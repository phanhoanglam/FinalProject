package com.spring.Final.modules.jobs;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.job_proposal.projections.JobProposalList;
import com.spring.Final.modules.jobs.dtos.JobDTO;
import com.spring.Final.modules.jobs.projections.JobDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/jobs")
public class JobController extends ApiController {

    private final JobService service;

    public JobController(JobService service) {
        this.service = service;
    }

//    @GetMapping("")
//    public ResponseEntity<ApiResult> list(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size,
//            SearchJobDTO model
//    ) {
//        Page<JobList> data = service.list(page, size, model);
//
//        return buildResponse(data);
//    }

//    @GetMapping("/{slug}")
//    public ResponseEntity<ApiResult> getDetail(@PathVariable(value = "slug") String slug) {
//        JobDetail data = service.getDetail(slug);
//
//        return buildResponse(data);
//    }

    @GetMapping("/{slug}/job-proposals")
    public ResponseEntity<ApiResult> listProposals(@PathVariable(value = "slug") String slug) {
        List<JobProposalList> data = service.listProposals(slug);

        return buildResponse(data);
    }

//    @PostMapping("")
//    public ResponseEntity<ApiResult> create(@Valid @RequestBody JobDTO model) throws IOException {
//        HttpServletRequest request = this.getCurrentRequest();
//        HashMap<String, Object> employer = this.getCurrentUser(request);
//        model.setEmployerId((Integer) employer.get("id"));
//
//        JobDetail data = service.createOne(model);
//
//        return buildResponse(data);
//    }

//    @PutMapping("/{id}")
//    public ResponseEntity<ApiResult> update(@Valid @RequestBody JobDTO model, @PathVariable(value = "id") int id) throws IOException {
//        model.setId(id);
//        JobDetail data = service.updateOne(model);
//
//        return buildResponse(data);
//    }

//    @DeleteMapping("/{id}")
//    public @ResponseBody ResponseEntity<ApiResult> delete(@PathVariable(value = "id") int id) {
//        service.deleteOne(id);
//
//        return buildResponse(null);
//    }
}
