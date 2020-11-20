package com.spring.Final.modules.job_proposal.projections;


import lombok.Data;

@Data
public class Employee {
    private int id;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private String address;

    private String nationality;

    private String phone;

    private String slug;

    private float rating;

    public void setRating(float rating) {
        this.rating = Float.parseFloat(String.format("%.1f", rating));
    }
}
