package com.spring.Final.modules.employee;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.employee.dtos.SearchEmployeeDTO;
import com.spring.Final.modules.employee.projections.EmployeeGetDetail;
import com.spring.Final.modules.employee.projections.EmployeeList;
import com.spring.Final.modules.review.projections.ReviewList;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/employees")
public class EmployeeController extends ApiController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

//    @PostMapping("/register")
//    public ResponseEntity<ApiResult> register(@Valid @RequestBody RegisterDTO model) {
//        EmployeeDetail data = service.register(model);
//
//        return buildResponse(data);
//    }

    @GetMapping("")
    public ResponseEntity<ApiResult> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            SearchEmployeeDTO model
    ) {
        Page<EmployeeList> data = service.list(page, size, model);

        return buildResponse(data);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResult> getDetail(@PathVariable(value = "slug") String slug) {
        EmployeeGetDetail data = service.getDetail(slug);

        return buildResponse(data);
    }

    @GetMapping("/{slug}/reviews")
    public ResponseEntity<ApiResult> listReviews(
            @PathVariable(value = "slug") String slug,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size
    ) {
        Page<ReviewList> data = service.listReviews(page, size, slug);

        return buildResponse(data);
    }
}
