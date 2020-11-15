package com.spring.Final.modules.employer;

import com.spring.Final.core.exceptions.BaseException;
import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.employer.dtos.LoginDTO;
import com.spring.Final.modules.jobs.projections.JobManage;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/employers")
public class EmployerController extends ApiController {

    private final EmployerService employerService;

    public EmployerController(
            EmployerService employerService
    ) {
        this.employerService = employerService;
    }

//    @PostMapping("/login")
//    public ResponseEntity<ApiResult> login(@Valid @RequestBody LoginDTO model) {
//        HashMap<String, Object> data = new HashMap<>();
//        try {
//            data = employerService.login(model.getEmail(), model.getPassword());
//
//            return buildResponse(data);
//        } catch (BaseException e) {
//            data.put("message", e.getMessage());
//
//            return buildResponse(e.getHttpStatus(), data, e.getCode());
//        }
//    }

//    @PostMapping("/register")
//    public ResponseEntity<ApiResult> login(@Valid @RequestBody RegisterDTO model) {
//        try {
//            EmployerDetail data = employerService.register(model);
//
//            return buildResponse(data);
//        } catch (BaseException e) {
//            HashMap<String, String> data = new HashMap<>();
//            data.put("message", e.getMessage());
//
//            return buildResponse(e.getHttpStatus(), data, e.getCode());
//        } catch (Exception e) {
//            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, e.getLocalizedMessage());
//        }
//    }

    @GetMapping("/me/jobs")
    public ResponseEntity<ApiResult> listJobs(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        HttpServletRequest request = this.getCurrentRequest();
        HashMap<String, Object> employee = this.getCurrentUser(request);
        int id = (Integer) employee.get("id");

        Page<JobManage> results = this.employerService.listJobs(page, size, id);

        return buildResponse(results);
    }
}
