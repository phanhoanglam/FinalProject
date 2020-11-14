package com.spring.Final.modules.membership;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MembershipRepository extends JpaRepository<MembershipEntity, Integer> {
}
