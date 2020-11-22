package com.spring.Final.modules.notification;

import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.jobs.JobService;
import com.spring.Final.modules.jobs.projections.JobDetail;
import com.spring.Final.modules.notification.projections.Notification;
import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService extends ApiService<NotificationEntity, NotificationRepository> {
    @Autowired
    private JobService jobService;

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

    public List<Notification> findAllByUser(int userId, String role) {
        UserType userType = role.equals("employee") ? UserType.EMPLOYEE : UserType.EMPLOYER;

        List<NotificationEntity> notifications = this.repository.findAllByToUserIdAndToUserTypeAndIsRead(userId, userType, false);
        List<Notification> results = notifications.stream()
                .map(e -> this.modelMapper.map(e, Notification.class))
                .collect(Collectors.toList());
        int index = 0;

        for (Notification notification: results) {
            if (notification.getReferenceType() == ReferenceType.JOB_PROPOSAL) {
                notification.setUrl("/dashboard/manage-proposals");
            } else if (notification.getReferenceType() == ReferenceType.REVIEW) {
                notification.setUrl("/dashboard/reviews");
            } else if (notification.getReferenceType() == ReferenceType.JOB) {
                JobDetail jobDetail = this.jobService.getById(notification.getReferenceId());

                if (jobDetail == null) {
                    notifications.remove(index);
                } else {
                    notification.setUrl("/dashboard/manage-jobs/" + jobDetail.getSlug() + "/proposals");
                }
            }

            index += 1;
        }

        return results;
    }

    public void setRead(int[] ids) {
        this.repository.setRead(ids);
    }
}
