package com.spring.Final.modules.review;

import com.spring.Final.modules.job_proposal.JobProposalEntity;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.hibernate.boot.model.source.spi.Sortable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Integer> {
    Page<ReviewEntity> findAllByUserIdAndUserType(int userId, UserType userType, Pageable pageable);

    Page<ReviewEntity> findAllByToUserIdAndToUserType(int userId, UserType userType, Pageable pageable);

    List<ReviewEntity> findAllByToUserIdAndToUserType(int userId, UserType userType, Sort sortable);

    ReviewEntity findTopByJobProposalAndUserIdAndUserType(JobProposalEntity jobProposal, int userId, UserType userType);

    ReviewEntity findTopByUserTypeAndJobProposal(UserType userType, JobProposalEntity jobProposal);

    @Query(value = "select avg(r.rating) from Review r where r.toUserId = ?1 and r.toUserType = ?2")
    float recalculateRating(int toUserId, UserType toUserType);

    @Query(value = "select count(r) from Review r where r.toUserId = ?1 and r.toUserType = ?2 and r.deliveredOnTime = true")
    long countJobDoneOnTime(int toUserId, UserType toUserType);

    @Query(value = "select count(r) from Review r where r.toUserId = ?1 and r.toUserType = ?2 and r.deliveredOnBudget = true")
    long countJobDoneOnBudget(int toUserId, UserType toUserType);

    @Query(value = "select count(r) from Review r where r.toUserId = ?1 and r.toUserType = ?2")
    long countReviewsByUser(int toUserId, UserType toUserType);
}
