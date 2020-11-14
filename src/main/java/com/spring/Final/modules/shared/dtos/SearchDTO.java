package com.spring.Final.modules.shared.dtos;

import lombok.Data;

@Data
public class SearchDTO {
    private String[] names = new String[]{};
    private String location;
    private Integer[] categories = new Integer[]{};
    private Integer[] types = new Integer[]{};
    private Integer[] skills = new Integer[]{};
    private float salaryFrom;
    private float salaryTo;
}
