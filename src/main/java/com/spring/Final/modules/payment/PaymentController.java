package com.spring.Final.modules.payment;

import com.spring.Final.core.infrastructure.ApiController;
import com.spring.Final.core.infrastructure.ApiResult;
import com.spring.Final.modules.auth.CustomUserDetails;
import com.spring.Final.modules.payment.dtos.CreatePaymentDTO;
import com.spring.Final.modules.payment.projections.CreatedPayment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;

@RestController
@CrossOrigin
@RequestMapping("/api/payments")
public class PaymentController extends ApiController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("")
    public ResponseEntity<ApiResult> create(Authentication authentication, @Valid @RequestBody CreatePaymentDTO model) {
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        int id = (int) user.getInformation().get("id");

        model.setEmployerId(id);
        CreatedPayment payment = this.service.createOne(model);

        return buildResponse(payment);
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResult> handleWebhook(@Valid @RequestBody String json) throws IOException {
        HashMap<String, Boolean> result = this.service.handleWebhook(json);

        return buildResponse(result);
    }
}
