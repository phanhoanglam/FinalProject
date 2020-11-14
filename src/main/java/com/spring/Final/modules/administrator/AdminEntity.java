package com.spring.Final.modules.administrator;

import com.spring.Final.core.BaseEntity;
import com.spring.Final.core.helpers.CommonHelper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Admin")
@Table(name="administrators")
public class AdminEntity extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -1966936719506202280L;

    @Column(name = "email", length = 250, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "name", length = 250, nullable = true)
    private String name;

    @Column(name = "avatar", length = 250)
    private String avatar;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = CommonHelper.getCurrentTime();

    @Column(name = "updated_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = CommonHelper.getCurrentTime();

}
