package com.spring.Final.modules.notification.projections;

import com.spring.Final.modules.shared.enums.notification_reference_type.ReferenceType;
import lombok.Data;

@Data
public class Notification {
    private int id;
    private int referenceId;
    private ReferenceType referenceType;
    private String message;

    private String url;
}
