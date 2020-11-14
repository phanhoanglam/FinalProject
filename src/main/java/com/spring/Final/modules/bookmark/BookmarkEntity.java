package com.spring.Final.modules.bookmark;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.modules.shared.enums.bookmark_reference_type.ReferenceType;
import com.spring.Final.modules.shared.enums.bookmark_reference_type.ReferenceTypeAttributeConverter;
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
@Entity(name = "Bookmark")
@Table(name = "bookmarks")
public class BookmarkEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "user_type", nullable = false)
    @Convert(converter = UserTypeAttributeConverter.class)
    private UserType userType;

    @Column(name = "reference_id", nullable = false)
    private Integer referenceId;

    @Column(name = "reference_type", nullable = false)
    @Convert(converter = ReferenceTypeAttributeConverter.class)
    private ReferenceType referenceType;

    @Column(name = "status")
    private boolean status;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

}
