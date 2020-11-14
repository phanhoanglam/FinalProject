package com.spring.Final.modules.job_type;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/job-types")
public class JobTypeController extends ApiController {

    private final JobTypeService service;

    public JobTypeController(JobTypeService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<ApiResult> list() {
        List<NameOnly> data = service.findAllAsNameOnly();

        return buildResponse(data);
    }
}
