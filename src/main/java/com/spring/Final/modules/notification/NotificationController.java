package com.spring.Final.modules.notification;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.notification.dtos.SetReadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/notifications")
public class NotificationController extends ApiController {
    @Autowired
    private NotificationService service;

    @PostMapping("")
    public ResponseEntity<ApiResult> setRead(@Valid @RequestBody SetReadDTO dto) {
        this.service.setRead(dto.getIds());

        return buildResponse(new HashMap<String, String>());
    }
}
