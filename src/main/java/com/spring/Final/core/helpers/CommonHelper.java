package com.spring.Final.core.helpers;

import com.github.slugify.Slugify;
import com.google.gson.Gson;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

public class CommonHelper {
    public static String toSlug(String input) {
        Slugify slg = new Slugify();
        return slg.slugify(input);
    }

    public static Timestamp getCurrentTime() {
        Date date = new Date();
        return new Timestamp(date.getTime());
    }

    // function to generate a random string of length n
    public static String getAlphaNumericString(int n) {
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (alphaNumericString.length() * Math.random());
            sb.append(alphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public static String toJSON(Object data) {
        Gson gson = new Gson();

        return gson.toJson(data);
    }

    public static Point createGeometryPoint(HashMap<String, String> data) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(
                Double.parseDouble(data.get("x")),
                Double.parseDouble(data.get("y"))
        ));
        point.setSRID(geometryFactory.getSRID());

        return point;
    }

    public static Point createGeometryPoint(double lat, double lng) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        Point point = geometryFactory.createPoint(new Coordinate(lat, lng));
        point.setSRID(geometryFactory.getSRID());

        return point;
    }
}
