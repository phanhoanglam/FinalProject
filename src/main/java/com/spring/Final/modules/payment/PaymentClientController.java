package com.spring.Final.modules.payment;

import com.spring.Final.modules.payment.projections.PaymentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/payments")
public class PaymentClientController {
    @Autowired
    private PaymentService service;

    @GetMapping("/{id}")
    public String renderInvoice(@PathVariable int id, Model modelView) {
        PaymentData data = this.service.getPaymentData(id);
        modelView.addAttribute("detail", data);

        return "client/modules/payments/invoice";
    }
}
