package com.spring.Final.modules.job_category;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.job_category.projections.NameWithJobCount;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/job-categories")
public class JobCategoryController extends ApiController {

    private final JobCategoryService service;

    public JobCategoryController(JobCategoryService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<ApiResult> list() {
        List<NameWithJobCount> data = service.findAllAsNameOnly();

        return buildResponse(data);
    }
}
