package com.spring.Final.modules.shared.dtos;

import lombok.Data;

import java.util.List;

@Data
public class GeoCodeResponse {
    private List<GeoCode> results;
}
