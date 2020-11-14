package com.spring.Final.modules.message;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.shared.enums.user_type.UserType;
import com.spring.Final.modules.shared.enums.user_type.UserTypeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Message")
@Table(name = "messages")
public class MessageEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "userType", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType userType;

    @Column(name = "toUserId", nullable = false)
    private Integer toUserId;

    @Column(name = "toUserType", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType toUserType;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

}
