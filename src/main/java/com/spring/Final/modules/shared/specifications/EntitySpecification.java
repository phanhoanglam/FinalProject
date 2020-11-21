package com.spring.Final.modules.shared.specifications;

import com.spring.Final.core.common.MapUtils;
import com.spring.Final.core.exceptions.InvalidAddressException;
import com.spring.Final.modules.shared.dtos.SearchDTO;
import org.hibernate.spatial.predicate.SpatialPredicates;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Document: https://stackoverflow.com/questions/48346341/spring-boot-jpa-implementing-search-queries-with-optional-ranged-criteria
 * @Document: https://www.baeldung.com/java-http-request
 */
public class EntitySpecification<T> implements Specification<T> {
    protected final SearchDTO dto;

    public EntitySpecification(SearchDTO dto) {
        this.dto = dto;
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
        if (dto.getLocation() != null) {
            try {
                Predicate location = this.searchLocation(dto.getLocation(), cb, root);

                if (location != null) {
                    andPredicates.add(location);
                }
            } catch (IOException ignored) {}
        }

        return andPredicates;
    }

    protected Predicate searchLocation(String location, CriteriaBuilder cb, Root<T> root) throws IOException {
        Coordinate coordinate;

        try {
            coordinate = MapUtils.getCoordinateByText(location);
        } catch (InvalidAddressException e) {
            return null;
        }
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory(new GeometryFactory(new PrecisionModel(), 4326));
        shapeFactory.setCentre(coordinate);
        shapeFactory.setHeight(0.1);  // TODO: Set temporarily, refactor later
        shapeFactory.setWidth(0.1);

        return SpatialPredicates.within(cb, root.get("addressLocation"), shapeFactory.createRectangle());
    }
}
