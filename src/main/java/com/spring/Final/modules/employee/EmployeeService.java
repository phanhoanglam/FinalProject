package com.spring.Final.modules.employee;

import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.exceptions.*;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employee.dtos.RegisterDTO;
import com.spring.Final.modules.employee.dtos.SearchEmployeeDTO;
import com.spring.Final.modules.employee.projections.EmployeeDetail;
import com.spring.Final.modules.employee.projections.EmployeeGetDetail;
import com.spring.Final.modules.employee.projections.EmployeeList;
import com.spring.Final.modules.employee.specifications.EmployeeSpecification;
import com.spring.Final.modules.job_proposal.JobProposalService;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.review.projections.ReviewList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public EmployeeDetail register(RegisterDTO data) {
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
        newEmployee = this.repository.save(newEmployee);

        return this.modelMapper.map(newEmployee, EmployeeDetail.class);
    }

    public PageImpl<EmployeeList> list(int pageNumber, int size, SearchEmployeeDTO model) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));
        EmployeeSpecification search = new EmployeeSpecification(model);

        Page<EmployeeEntity> results = this.repository.findAll(search, page);
        List<EmployeeList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, EmployeeList.class))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    public EmployeeGetDetail getDetail(String slug) {
        EmployeeGetDetail data = this.repository.findBySlug(slug);
        if (data == null) {
            throw new ResourceNotFoundException();
        }
        data.setEmploymentHistory(this.jobProposalService.getEmploymentHistory(data.getId()));

        return data;
    }

    public Page<ReviewList> listReviews(int page, int size, String slug) {
        EmployeeGetDetail data = this.repository.findBySlug(slug);
        if (data == null) {
            throw new ResourceNotFoundException();
        }
        Page<ReviewList> results = this.reviewService.listByEmployee(page, size, data.getId());

        return results;
    }
}