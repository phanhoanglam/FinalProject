package com.spring.Final.modules.jobs;

import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.jobs.dtos.SearchJobDTO;
import com.spring.Final.modules.jobs.projections.JobList;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/jobs")
public class JobClientController {

    private final JobService service;

    public JobClientController(JobService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<ApiResult> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            SearchJobDTO model
    ) {
        Page<JobList> data = service.list(page, size, model);

        return buildResponse(data);
    }
}
