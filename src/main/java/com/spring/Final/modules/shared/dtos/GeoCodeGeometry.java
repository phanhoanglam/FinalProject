package com.spring.Final.modules.shared.dtos;

import lombok.Data;

import java.util.HashMap;

@Data
public class GeoCodeGeometry {
    private HashMap<String, String> location;
}
