package com.spring.Final.modules.review;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.review.dtos.ReviewEmployeeDTO;
import com.spring.Final.modules.review.dtos.ReviewEmployerDTO;
import com.spring.Final.modules.review.projections.ReviewList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/api/reviews")
public class ReviewController extends ApiController {
    @Autowired
    private ReviewService service;

    @PostMapping("/employees")
    public ResponseEntity<ApiResult> reviewEmployee(@Valid @RequestBody ReviewEmployeeDTO model) {
        ReviewList review = this.service.reviewEmployee(model);

        return buildResponse(review);
    }

    @PostMapping("/employers")
    public ResponseEntity<ApiResult> reviewEmployer(@Valid @RequestBody ReviewEmployerDTO model) {
        ReviewList review = this.service.reviewEmployer(model);

        return buildResponse(review);
    }
}
