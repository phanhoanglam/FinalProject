package com.spring.Final.modules.payment;

import com.spring.Final.core.exceptions.ResourceNotFoundException;
import com.spring.Final.core.helpers.CommonHelper;
import com.spring.Final.core.infrastructure.ApiService;
import com.spring.Final.modules.employer.EmployerService;
import com.spring.Final.modules.membership.MembershipEntity;
import com.spring.Final.modules.membership.MembershipService;
import com.spring.Final.modules.payment.dtos.CreatePaymentDTO;
import com.spring.Final.modules.payment.exceptions.InvalidMembershipException;
import com.spring.Final.modules.payment.exceptions.PaymentFailedException;
import com.spring.Final.modules.payment.exceptions.SerializeFailedException;
import com.spring.Final.modules.payment.exceptions.UnhandledEventTypeException;
import com.spring.Final.modules.payment.projections.CreatedPayment;
import com.spring.Final.modules.shared.enums.payment_status.PaymentStatus;
import com.spring.Final.modules.shared.enums.payment_status.PaymentStatusAttributeConverter;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService extends ApiService<PaymentEntity, PaymentRepository> {
    @Autowired
    private MembershipService membershipService;

    @Autowired
    private EmployerService employerService;

    public PaymentService(PaymentRepository repository) {
        Stripe.apiKey = "sk_test_51Hj4WbF55X8hSDUq8yu78ojXOHhWRgjnSwxd6TcUKetyiQf2EIa5ERbbSytTVG2kpP8n0d94O0RBFcHEqDwxTZSb00nPEgybsp";

        this.repository = repository;
    }

    public CreatedPayment createOne(CreatePaymentDTO data) {
        MembershipEntity membership = this.membershipService.getOne(data.getMembershipId());
        PaymentIntent paymentIntent;

        if (membership == null) {
            throw new ResourceNotFoundException();
        }
        try {
            List<Object> paymentMethodTypes = new ArrayList<>();
            paymentMethodTypes.add("card");

            Map<String, Object> params = new HashMap<>();
            params.put("amount", membership.getPrice().multiply(BigDecimal.valueOf(100)));
            params.put("currency", "usd");
            params.put("payment_method_types", paymentMethodTypes);

            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            throw new PaymentFailedException();
        }
        PaymentEntity payment = new PaymentEntity();
        payment.setEmployer(this.employerService.getOne(data.getEmployerId()));
        payment.setMembership(membership);
        payment.setStripeId(paymentIntent.getId());
        payment.setAmount(membership.getPrice());
        payment.setStatus(PaymentStatusAttributeConverter.convertFromStripeStatus(paymentIntent.getStatus()));
        payment.setCreatedAt(CommonHelper.getCurrentTime());
        payment.setUpdatedAt(CommonHelper.getCurrentTime());
        payment = this.repository.save(payment);

        CreatedPayment formattedPayment = this.modelMapper.map(payment, CreatedPayment.class);
        formattedPayment.setSecret(paymentIntent.getClientSecret());

        return formattedPayment;
    }

    public HashMap<String, Boolean> handleWebhook(String json) {
        Event event = ApiResource.GSON.fromJson(json, Event.class);

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject;
        PaymentIntent paymentIntent;

        if (dataObjectDeserializer.getObject().isPresent()) {
            stripeObject = dataObjectDeserializer.getObject().get();
        } else {
            throw new SerializeFailedException();
        }
        switch (event.getType()) {
            case "payment_intent.succeeded":
                paymentIntent = (PaymentIntent) stripeObject;
                break;
            default:
                throw new UnhandledEventTypeException();
        }
        PaymentEntity payment = this.repository.findByStripeId(paymentIntent.getId());
        PaymentStatus status = PaymentStatusAttributeConverter.convertFromStripeStatus(paymentIntent.getStatus());

        if (payment == null) {
            throw new ResourceNotFoundException();
        }
        if (status == PaymentStatus.succeeded) {
            this.employerService.updateMembership(payment.getEmployer().getId(), payment.getMembership());
        }
        payment.setStatus(status);
        this.repository.save(payment);

        HashMap<String, Boolean> result = new HashMap<>();
        result.put("success", true);

        return result;
    }
}
