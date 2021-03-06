package com.spring.Final.modules.employer;

import com.spring.Final.core.common.MapUtils;
import com.spring.Final.core.exceptions.EntityExistException;
import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.auth.dtos.RegisterDTO;
import com.spring.Final.modules.employer.projections.*;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.projections.JobManage;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.review.ReviewService;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployerService extends ApiService<EmployerEntity, EmployerRepository> {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JobService jobService;

    @Autowired
    private ReviewService reviewService;

    public EmployerService(
            EmployerRepository repository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public PageImpl<EmployerList> list(int pageNumber, int size) {
        Pageable page = PageRequest.of(this.getPage(pageNumber), size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<EmployerEntity> results = this.repository.findAll(page);
        List<EmployerList> resultList = results.stream()
                .map(e -> this.modelMapper.map(e, EmployerList.class))
                .collect(Collectors.toList());

        return new PageImpl<>(resultList, results.getPageable(), results.getTotalElements());
    }

    public EmployerDetailData getDetail(String slug) {
        EmployerEntity employer = this.repository.findBySlug(slug);

        if (employer == null) {
            throw new ResourceNotFoundException();
        }

        return new EmployerDetailData(
                this.modelMapper.map(employer, EmployerDetail.class),
                this.jobService.listByEmployer(employer),
                this.reviewService.listByUser(employer.getId(), UserType.EMPLOYER)
        );
    }

    public EmployerEntity register(RegisterDTO data) {
        EmployerEntity newEmployer = this.modelMapper.map(data, EmployerEntity.class);
        EmployerEntity employer = this.repository.findByEmail(newEmployer.getEmail());

        if (employer != null) {
            throw new EntityExistException("This email is already used");
        }
        newEmployer.setPassword(this.passwordEncoder.encode(newEmployer.getPassword()));
        newEmployer.setAddressLocation(CommonHelper.createGeometryPoint(data.getAddressLocation()));
        newEmployer.setSlug(CommonHelper.toSlug(newEmployer.getEmail()));
        newEmployer.setCreatedAt(CommonHelper.getCurrentTime());
        newEmployer.setUpdatedAt(CommonHelper.getCurrentTime());
        newEmployer.setVerified(true);

        return this.repository.save(newEmployer);
    }

    public void increaseJobsCount(int id) {
        this.repository.increaseJobsCount(id);
    }

    public void updateMembership(int id, MembershipEntity membership) {
        Timestamp expiredAt = CommonHelper.getCurrentTime();
        MembershipDuration duration = membership.getDuration();

        if (duration == MembershipDuration.MONTHLY) {
            expiredAt.setTime(expiredAt.getTime() + 30L * 24 * 60 * 60 * 1000);
        } else if (duration == MembershipDuration.YEARLY) {
            expiredAt.setTime(expiredAt.getTime() + 365L * 24 * 60 * 60 * 1000);
        }
        EmployerEntity employer = this.repository.getOne(id);
        employer.setMembership(membership);
        employer.setMembershipEndAt(expiredAt);
        this.repository.save(employer);
    }

    public Page<JobManage> listJobs(int page, int size, int id) {
        EmployerEntity employer = this.repository.getOne(id);

        return this.jobService.listByEmployer(page, size, employer);
    }

    public void updateRating(int id, float rating) {
        EmployerEntity employer = this.repository.findById(id).orElse(null);

        if (employer != null) {
            employer.setRating(rating);
            this.repository.save(employer);
        }
    }

    public EmployerProfile getOnlyProfile(int id) {
        Optional<EmployerEntity> result = this.repository.findById(id);

        return result.map(x -> this.modelMapper.map(x, EmployerProfile.class)).get();
    }

    public boolean submitProfile(EmployerProfile dto) throws IOException {
        dto.setAddressLocation(MapUtils.getCoordinateByText(dto.getAddress()));

        EmployerEntity employer = this.repository.findById(dto.getId()).get();
        employer.setName(dto.getName());
        employer.setPhone(dto.getPhone());
        employer.setAvatar(dto.getAvatar());
        employer.setAddress(dto.getAddress());
        employer.setNationality(dto.getNationality());
        employer.setDescription(dto.getDescription());
        employer.setAddressLocation(CommonHelper.createGeometryPoint(dto.getAddressLocation()));

        if (!dto.getPassword().isEmpty() && dto.getPassword() != null) {
            if (!this.passwordEncoder.matches(dto.getPassword(), employer.getPassword())) {
                return false;
            }
            employer.setPassword(this.passwordEncoder.encode(dto.getNewPassword()));
        }
        this.repository.save(employer);

        return true;
    }

    public EmployerProfile getById(int id) {
        EmployerEntity employer = this.repository.findById(id).get();

        return this.modelMapper.map(employer, EmployerProfile.class);
    }

    public EmployerMembership getMembership(int employerId) {
        EmployerEntity employer = this.repository.findById(employerId).get();

        return this.modelMapper.map(employer, EmployerMembership.class);
    }
}
