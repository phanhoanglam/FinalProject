package com.spring.Final.modules.shared.specifications;

import com.google.gson.Gson;
import com.spring.Final.core.helpers.HttpRequestService;
import com.spring.Final.modules.shared.dtos.GeoCodeGeometry;
import com.spring.Final.modules.shared.dtos.GeoCodeResponse;
import com.spring.Final.modules.shared.dtos.SearchDTO;
import org.hibernate.spatial.predicate.SpatialPredicates;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * @Document: https://stackoverflow.com/questions/48346341/spring-boot-jpa-implementing-search-queries-with-optional-ranged-criteria
 * @Document: https://www.baeldung.com/java-http-request
 */
public class EntitySpecification<T> implements Specification<T> {
    private String API_KEY = "AIzaSyBYiHfMlwJdYaxFTkZQAk57bZaLPPW35TY";

    protected final SearchDTO dto;

    protected final HttpRequestService httpRequestService;

    public EntitySpecification(SearchDTO dto) {
        this.dto = dto;
        this.httpRequestService = new HttpRequestService();
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
        Predicate predicate = cb.disjunction();
        ArrayList<Predicate> andPredicates = this.getSearchPredicates(new ArrayList<>(), root, cb);
        Predicate[] predicates = new Predicate[andPredicates.size()];
        predicates = andPredicates.toArray(predicates);
        predicate.getExpressions().add(cb.and(predicates));

        return predicate;
    }

    public ArrayList<Predicate> getSearchPredicates(ArrayList<Predicate> andPredicates, Root<T> root, CriteriaBuilder cb) {
        if (dto.getSalaryFrom() != 0) {
            andPredicates.add(
                    cb.greaterThanOrEqualTo(root.get("salaryFrom"), dto.getSalaryFrom())
            );
        }
        if (dto.getSalaryTo() != 0) {
            andPredicates.add(
                    cb.lessThanOrEqualTo(root.get("salaryTo"), dto.getSalaryTo())
            );
        }
        if (dto.getLocation() != null) {
            try {
                andPredicates.add(
                        this.searchLocation(dto.getLocation(), cb, root)
                );
            } catch (IOException ignored) {}
        }

        return andPredicates;
    }

    protected Predicate searchLocation(String location, CriteriaBuilder cb, Root<T> root) throws IOException {
        String geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + URLEncoder.encode(location) + "&key=" + this.API_KEY;
        String json = httpRequestService.get(geoUrl);

        Gson gson = new Gson();
        GeoCodeResponse response = gson.fromJson(json, GeoCodeResponse.class);
        GeoCodeGeometry geometry = response.getResults().get(0).getGeometry();

        double lat = Double.parseDouble(geometry.getLocation().get("lat"));
        double lng = Double.parseDouble(geometry.getLocation().get("lng"));
        Coordinate coordinate = new Coordinate(lat, lng);

        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setCentre(coordinate);
        shapeFactory.setHeight(0.0452185866);  // TODO: Set temporarily, refactor later
        shapeFactory.setWidth(0.0452185866);
        Polygon rectangle = shapeFactory.createRectangle();

        return SpatialPredicates.within(cb, root.get("addressLocation"), rectangle);
    }
}
