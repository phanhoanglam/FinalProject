package com.spring.Final.modules.employee;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.common.MapUtils;
import com.spring.Final.core.exceptions.*;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.auth.dtos.RegisterDTO;
import com.spring.Final.modules.employee.dtos.SearchEmployeeDTO;
import com.spring.Final.modules.employee.projections.*;
import com.spring.Final.modules.employee.specifications.EmployeeSpecification;
import com.spring.Final.modules.job_category.JobCategoryService;
import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import com.spring.Final.modules.shared.specifications.File;
import com.spring.Final.modules.skill.SkillService;
import com.spring.Final.modules.skill.projections.Skill;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService extends ApiService<EmployeeEntity, EmployeeRepository> {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JobProposalService jobProposalService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JobCategoryService jobCategoryService;

    @Autowired
    private SkillService skillService;

    public EmployeeService(
            EmployeeRepository repository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public HashMap<String, Object> login(String email, String password) {
        EmployeeEntity employee = this.repository.findByEmail(email);

        if (employee == null || !this.passwordEncoder.matches(password, employee.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }
        if (employee.isBlocked()) {
            throw new AccountBlockedException();
        }
        if (!employee.isVerified()) {
            throw new AccountUnverifiedException();
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", employee.getId());
        data.put("email", employee.getEmail());
        data.put("firstName", employee.getFirstName());
        data.put("lastName", employee.getLastName());
        data.put("phone", employee.getPhone());
        data.put("avatar", employee.getAvatar());
        data.put("address", employee.getAddress());
        data.put("token", JwtHelper.generateBearerToken(employee.getId(), "employee"));
        data.put("slug", employee.getSlug());

        if (employee.getAddressLocation() != null) {
            HashMap<String, Double> coordinate = new HashMap<>();
            coordinate.put("x", employee.getAddressLocation().getX());
            coordinate.put("y", employee.getAddressLocation().getY());
            data.put("addressLocation", coordinate);
        }

        return data;
    }

    public EmployeeEntity register(RegisterDTO data) {
        EmployeeEntity newEmployee = this.modelMapper.map(data, EmployeeEntity.class);
        EmployeeEntity employee = this.repository.findByEmail(newEmployee.getEmail());

        if (employee != null) {
            throw new EntityExistException("This email is already used");
        }
        newEmployee.setAddressLocation(CommonHelper.createGeometryPoint(data.getAddressLocation()));
        newEmployee.setPassword(this.passwordEncoder.encode(newEmployee.getPassword()));
        newEmployee.setSlug(CommonHelper.toSlug(newEmployee.getEmail()));
        newEmployee.setCreatedAt(CommonHelper.getCurrentTime());
        newEmployee.setUpdatedAt(CommonHelper.getCurrentTime());
        newEmployee.setVerified(true);  // TODO: Temporarily for testing

        return this.repository.save(newEmployee);
    }

    public ListEmployeesData list(int pageNumber, int size, SearchEmployeeDTO model) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        EmployeeSpecification search = new EmployeeSpecification(model);

        Page<EmployeeEntity> results = this.repository.findAll(search, page);
        List<EmployeeList> resultList = results.stream()
                .map(e -> {
                    EmployeeList employee = this.modelMapper.map(e, EmployeeList.class);
                    employee.setSuccessRate(this.jobProposalService.calculateSuccessRate(e.getId()));

                    return employee;
                })
                .collect(Collectors.toList());
        List<NameWithJobCount> jobCategories = jobCategoryService.findAllAsNameOnly();

        return new ListEmployeesData(
                jobCategories,
                skillService.findAllAsNameOnly(
                        1,
                        100,
                        (model.getJobCategories().size() == 0 ? new int[]{jobCategories.get(0).getId()} : model.getJobCategories().stream().mapToInt(i -> i).toArray())
                ),
                new PageImpl<>(resultList, results.getPageable(), results.getTotalElements())
        );
    }

    public EmployeeDetailData getDetail(String slug) {
        EmployeeEntity employee = this.repository.findBySlug(slug);

        if (employee == null) {
            throw new ResourceNotFoundException();
        }
        EmployeeDetail data = this.modelMapper.map(employee, EmployeeDetail.class);
        data.setSuccessRate(this.jobProposalService.calculateSuccessRate(data.getId()));
        data.setJobHiredCount(this.jobProposalService.countJobHired(data.getId()));
        data.setJobDoneCount(this.jobProposalService.countJobDone(data.getId()));
        data.setJobDoneOnTime(this.reviewService.countJobDoneOnTime(data.getId()));
        data.setJobDoneOnBudget(this.reviewService.countJobDoneOnBudget(data.getId()));

        return new EmployeeDetailData(
                data,
                this.jobProposalService.getEmploymentHistory(data.getId()),
                this.reviewService.listByUser(data.getId(), UserType.EMPLOYEE)
        );
    }

    public void updateRating(int id, float rating) {
        EmployeeEntity employee = this.repository.findById(id).orElse(null);

        if (employee != null) {
            employee.setRating(rating);
            this.repository.save(employee);
        }
    }

    public ProfilePageData getProfile(int id) {
        EmployeeEntity result = this.repository.findById(id).get();
        EmployeeProfile profile = this.modelMapper.map(result, EmployeeProfile.class);

        profile.setJobCategoryIds(result.getJobCategories().stream().map(BaseEntity::getId).collect(Collectors.toList()));
        profile.setSkillIds(result.getSkills().stream().map(BaseEntity::getId).collect(Collectors.toList()));

        List<Skill> skills = skillService.findAllAsNameOnly(
                1, 1000, result.getJobCategories().stream().mapToInt(BaseEntity::getId).toArray())
                .stream()
                .map(x -> this.modelMapper.map(x, Skill.class))
                .collect(Collectors.toList());

        if (result.getAttachments() != null) {
            profile.setAttachmentsDecoded((ArrayList<File>) CommonHelper.parseJSON(result.getAttachments(), new ArrayList<>()));
        }

        return new ProfilePageData(
                profile,
                skills,
                jobCategoryService.findAllAsNameOnly()
        );
    }

    @Transactional
    public EmployeeEntity submitProfile(EmployeeProfile dto) throws IOException {
        EmployeeEntity employee = this.repository.findById(dto.getId()).get();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setPhone(dto.getPhone());
        employee.setAvatar(dto.getAvatar());
        employee.setAddress(dto.getAddress());
        employee.setNationality(dto.getNationality());
        employee.setJobTitle(dto.getJobTitle());
        employee.setDescription(dto.getDescription());
        employee.setMinHourlyRate(dto.getMinHourlyRate());
        employee.setSkills(this.skillService.getAllByIds(dto.getSkillIds()));
        employee.setJobCategories(this.jobCategoryService.getAllByIds(dto.getJobCategoryIds()));

        Coordinate coordinate = MapUtils.getCoordinateByText(dto.getAddress());
        employee.setAddressLocation(CommonHelper.createGeometryPoint(coordinate));

        if (dto.getAttachmentsDecoded() != null) {
            String attachments = CommonHelper.toJSON(dto.getAttachmentsDecoded());
            employee.setAttachments(attachments);
        }
        if (!dto.getPassword().isEmpty() && dto.getPassword() != null) {
            if (!this.passwordEncoder.matches(dto.getPassword(), employee.getPassword())) {
                return null;
            }
            employee.setPassword(this.passwordEncoder.encode(dto.getNewPassword()));
        }

        return this.repository.save(employee);
    }

    public EmployeeProfile getById(int id) {
        EmployeeEntity employee = this.repository.findById(id).get();

        return this.modelMapper.map(employee, EmployeeProfile.class);
    }
}
