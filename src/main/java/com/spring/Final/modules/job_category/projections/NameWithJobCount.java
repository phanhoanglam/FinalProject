package com.spring.Final.modules.job_category.projections;

import lombok.Data;

@Data
public class NameWithJobCount {
    private int id;
    private long jobCount;
    private String name;
    private String description;
    private String iconClass;

    public NameWithJobCount(int id, String name, long jobCount, String description, String iconClass) {
        this.id = id;
        this.name = name;
        this.jobCount = jobCount;
        this.description = description;
        this.iconClass = iconClass;
    }
}
