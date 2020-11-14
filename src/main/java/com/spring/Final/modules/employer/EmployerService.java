package com.spring.Final.modules.employer;

import com.spring.Final.core.common.JwtHelper;
import com.spring.Final.core.exceptions.AccountBlockedException;
import com.spring.Final.core.exceptions.AccountUnverifiedException;
import com.spring.Final.core.exceptions.EntityExistException;
import com.spring.Final.core.exceptions.InvalidEmailOrPasswordException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employer.dtos.RegisterDTO;
import com.spring.Final.modules.employer.projections.EmployerDetail;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.projections.JobManage;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.shared.enums.membership_duration.MembershipDuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;

@Service
public class EmployerService extends ApiService<EmployerEntity, EmployerRepository> {
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JobService jobService;

    public EmployerService(
            EmployerRepository repository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public HashMap<String, Object> login(String email, String password) {
        EmployerEntity employer = this.repository.findByEmail(email);

        if (employer == null || !this.passwordEncoder.matches(password, employer.getPassword())) {
            throw new InvalidEmailOrPasswordException();
        }
        if (employer.isBlocked()) {
            throw new AccountBlockedException();
        }
        if (!employer.isVerified()) {
            throw new AccountUnverifiedException();
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", employer.getId());
        data.put("email", employer.getEmail());
        data.put("name", employer.getName());
        data.put("phone", employer.getPhone());
        data.put("avatar", employer.getAvatar());
        data.put("address", employer.getAddress());
        data.put("token", JwtHelper.generateBearerToken(employer.getId(), "employer"));
        data.put("slug", employer.getSlug());

        if (employer.getAddressLocation() != null) {
            HashMap<String, Double> coordinate = new HashMap<>();
            coordinate.put("x", employer.getAddressLocation().getX());
            coordinate.put("y", employer.getAddressLocation().getY());
            data.put("addressLocation", coordinate);
        }

        return data;
    }

    public EmployerDetail register(RegisterDTO data) {
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
        newEmployer = this.repository.save(newEmployer);

        return this.modelMapper.map(newEmployer, EmployerDetail.class);
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
}
