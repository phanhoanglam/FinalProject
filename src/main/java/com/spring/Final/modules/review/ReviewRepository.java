package com.spring.Final.modules.review;

import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    Page<ReviewEntity> findAllByToUserIdAndToUserType(int userId, UserType userType, Pageable pageable);

    ReviewEntity findTopByJobProposalAndUserIdAndUserType(JobProposalEntity jobProposal, int userId, UserType userType);

    ReviewEntity findByUserTypeAndJobProposal(UserType userType, JobProposalEntity jobProposal);

    @Query(value = "select avg (r.rating) from Review r where r.toUserId = ?1 and r.toUserType = ?2")
    float recalculateRating(int toUserId, UserType toUserType);
}
