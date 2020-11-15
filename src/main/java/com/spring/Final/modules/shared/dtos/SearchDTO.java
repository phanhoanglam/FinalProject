package com.spring.Final.modules.shared.dtos;

import lombok.Data;

import java.util.ArrayList;

@Data
public class SearchDTO {
    private String[] names = new String[]{};
    private String location;
    private ArrayList<Integer> jobCategories = new ArrayList<>();
    private ArrayList<Integer> jobTypes = new ArrayList<>();
    private ArrayList<Integer> skills = new ArrayList<>();
    private float salaryFrom = 0;
    private float salaryTo = 300000;
}
