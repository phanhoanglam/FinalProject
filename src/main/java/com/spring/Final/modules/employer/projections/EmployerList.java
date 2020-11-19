package com.spring.Final.modules.employer.projections;

import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.util.HashMap;

@Data
public class EmployerList {
    private int id;
    private String name;
    private String avatar;
    private float rating;
    private String slug;
}
