package com.spring.Final.modules.skill;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.shared.dtos.NameOnly;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/skills")
public class SkillController extends ApiController {

    private final SkillService service;

    public SkillController(SkillService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<ApiResult> list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam() int[] jobCategories
    ) {
        Page<NameOnly> data = service.findAllAsNameOnly(page, size, jobCategories);

        return buildResponse(data);
    }
}
