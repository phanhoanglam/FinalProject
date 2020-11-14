package com.spring.Final.modules.medias;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import com.spring.Final.modules.shared.enums.user_type.UserTypeAttributeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

public class MediaEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "userType", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType userType;

    @Column(name = "mimeType", nullable = false)
    String mimeType;

    @Column(name = "url", nullable = false)
    String url;

    @Column(name = "createdAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

}
