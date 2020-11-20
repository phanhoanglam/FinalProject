package com.spring.Final.core.common;

import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.shared.dtos.SearchDTO;

import java.util.ArrayList;

public class General {
    public static String ConvertURL(SearchDTO dto){
        String url = "";
        if (dto.getNames().length > 0) {
            url += "&names=" + String.join(",", dto.getNames());
        }
        if (dto.getLocation() != null && dto.getLocation() != "") {
            url += "&location=" + dto.getLocation();
        }
        if (dto.getJobCategories().size() > 0) {
            ArrayList<String> Categories = new ArrayList<>();
            for (int i = 0; i < dto.getJobCategories().size(); i++) {
                Categories.add(dto.getJobCategories().get(i).toString());
            }
            url += "&jobCategories=" + String.join(",", Categories);
        }
        if (dto.getJobTypes().size() > 0) {
            ArrayList<String> type = new ArrayList<>();
            for (int i = 0; i < dto.getJobTypes().size(); i++) {
                type.add(dto.getJobTypes().get(i).toString());
            }
            url += "&jobTypes=" + String.join(",", type);
        }
        if (dto.getSkills().size() > 0) {
            ArrayList<String> skills = new ArrayList<>();
            for (int i = 0; i < dto.getSkills().size(); i++) {
                skills.add(dto.getSkills().get(i).toString());
            }
            url += "&skills=" + String.join(",", skills);
        }
        url += "&salaryFrom=" + dto.getSalaryFrom();
        url += "&salaryTo=" + dto.getSalaryTo();

        return url;
    }
}
