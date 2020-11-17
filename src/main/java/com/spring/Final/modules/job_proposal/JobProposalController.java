package com.spring.Final.modules.job_proposal;

import com.spring.Final.core.helpers.StorageService;
import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.job_proposal.dtos.ProposeJobDTO;
import com.spring.Final.modules.job_proposal.dtos.SearchProposalDTO;
import com.spring.Final.modules.job_proposal.projections.JobProposalDetailExistence;
import com.spring.Final.modules.job_proposal.projections.JobProposalList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/job-proposals")
public class JobProposalController extends ApiController {

    @Autowired
    private JobProposalService service;

    @Autowired
    private StorageService storageService;

    @PostMapping("")
    public ResponseEntity<ApiResult> proposeJob(Authentication authentication, @Valid @RequestBody ProposeJobDTO model) {
        HttpServletRequest request = this.getCurrentRequest();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        model.setEmployeeId((Integer) user.getInformation().get("id"));

        JobProposalList data = service.proposeJob(model);

        return buildResponse(data);
    }

    @PostMapping("/search")
    public ResponseEntity<ApiResult> searchProposal(@Valid @RequestBody SearchProposalDTO model) {
        HttpServletRequest request = this.getCurrentRequest();
        HashMap<String, Object> employee = this.getCurrentUser(request);
        model.setEmployeeId((Integer) employee.get("id"));

        JobProposalDetailExistence data = service.searchProposal(model);

        return buildResponse(data);
    }

    @PostMapping("/resume")
    public ResponseEntity<ApiResult> handleFileUpload(@RequestParam("file") MultipartFile file) {
        HashMap<String, String> data = storageService.store(file);

        return buildResponse(data);
    }

    @PutMapping("/{id}/acceptance")
    public ResponseEntity<ApiResult> accept(@PathVariable(value = "id") int id) {
        JobProposalList data = this.service.accept(id);

        return buildResponse(data);
    }

    @PutMapping("/{id}/rejection")
    public ResponseEntity<ApiResult> reject(@PathVariable(value = "id") int id) {
        JobProposalList data = this.service.reject(id);

        return buildResponse(data);
    }

    @PutMapping("/{id}/failure")
    public ResponseEntity<ApiResult> setFailed(@PathVariable(value = "id") int id) {
        JobProposalList data = this.service.setFailed(id);

        return buildResponse(data);
    }
}
