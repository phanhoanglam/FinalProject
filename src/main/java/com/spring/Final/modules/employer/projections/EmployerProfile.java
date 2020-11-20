package com.spring.Final.modules.employer.projections;

import lombok.Data;
import org.locationtech.jts.geom.Coordinate;

@Data
public class EmployerProfile {
    private int id;
    private String email;
    private String password;
    private String newPassword;
    private String name;
    private String phone;
    private String avatar;
    private String address;;
    private String nationality;
    private String description;
    private Coordinate addressLocation;
}
