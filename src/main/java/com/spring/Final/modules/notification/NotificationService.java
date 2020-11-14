package com.spring.Final.modules.notification;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.stereotype.Service;

@Service
public class NotificationService extends ApiService<NotificationEntity, NotificationRepository> {
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public void create(int toUserId, UserType toUserType, int referenceId, ReferenceType referenceType, String message) {
        NotificationEntity notification = new NotificationEntity();
        notification.setToUserId(toUserId);
        notification.setToUserType(toUserType);
        notification.setReferenceId(referenceId);
        notification.setReferenceType(referenceType);
        notification.setMessage(message);

        this.repository.save(notification);
    }
}
