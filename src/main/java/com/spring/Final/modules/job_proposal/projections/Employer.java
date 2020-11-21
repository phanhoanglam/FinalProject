package com.spring.Final.modules.job_proposal.projections;

import lombok.Data;

@Data
public class Employer {
    private int id;
    private String email;
    private String name;
    private String phone;
    private String avatar;
    private String address;
    private String nationality;
    private String description;
    private String slug;
    private float rating;
    public void setRating(float rating) {
        this.rating = Float.parseFloat(String.format("%.1f", rating));
    }
}
