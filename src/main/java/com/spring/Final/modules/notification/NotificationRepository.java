package com.spring.Final.modules.notification;

import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findAllByToUserIdAndToUserTypeAndIsRead(int userId, UserType userType, boolean isRead);

    @Transactional
    @Modifying
    @Query(value = "update Notification as n set n.isRead = 1 where n.id in ?1")
    void setRead(int[] ids);
}
