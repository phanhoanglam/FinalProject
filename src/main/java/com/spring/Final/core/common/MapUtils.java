package com.spring.Final.core.common;

import com.google.gson.Gson;
import com.spring.Final.core.exceptions.InvalidAddressException;
import com.spring.Final.core.helpers.HttpRequestService;
import com.spring.Final.modules.shared.dtos.GeoCodeGeometry;
import com.spring.Final.modules.shared.dtos.GeoCodeResponse;
import org.locationtech.jts.geom.Coordinate;

import java.io.IOException;
import java.net.URLEncoder;

public class MapUtils {
    private static String API_KEY = "AIzaSyBYiHfMlwJdYaxFTkZQAk57bZaLPPW35TY";

    private static final HttpRequestService httpRequestService = new HttpRequestService();

    public static Coordinate getCoordinateByText(String location) throws IOException {
        String geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(location) + "&key=" + MapUtils.API_KEY;
        String json = httpRequestService.get(geoUrl);

        Gson gson = new Gson();
        GeoCodeResponse response = gson.fromJson(json, GeoCodeResponse.class);

        if (response.getResults().size() == 0) {
            throw new InvalidAddressException();
        }
        GeoCodeGeometry geometry = response.getResults().get(0).getGeometry();

        double lat = Double.parseDouble(geometry.getLocation().get("lat"));
        double lng = Double.parseDouble(geometry.getLocation().get("lng"));

        return new Coordinate(lat, lng);
    }
}
