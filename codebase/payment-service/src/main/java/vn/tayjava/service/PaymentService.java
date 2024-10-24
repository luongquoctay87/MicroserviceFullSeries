package vn.tayjava.service;

import com.google.gson.Gson;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import vn.tayjava.common.PaymentStatus;
import vn.tayjava.controller.PaymentInfoRequest;
import vn.tayjava.model.Payment;
import vn.tayjava.model.PmtOrderMessage;
import vn.tayjava.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "PAYMENT-SERVICE")
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, KafkaTemplate<String, String> kafkaTemplate, @Value("${stripe.apiKey}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Charge charge(String token, double amount) throws StripeException {
        // Convert amount to cents
        int amountInCents = (int) (amount * 100);

        // Create a charge request
        ChargeCreateParams params = ChargeCreateParams.builder()
                .setAmount((long) amountInCents)
                .setCurrency("usd")
                .setDescription("Example charge")
                .setSource(token) // Token from the frontend
                .build();

        return Charge.create(params);
    }

    public ResponseEntity<String> stripePayment(String orderId) throws Exception {
        Payment payment = paymentRepository.findByOrderId(orderId);

        if (payment == null) {
            throw new Exception("Payment information is missing");
        }
        payment.setAmount(00.00);
        paymentRepository.save(payment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @KafkaListener(topics = "checkout-order-topic", groupId = "checkout-order-group")
    public void payForOrder(String message)  {
        log.info("checkoutInfo = {}", message);

        Gson gson = new Gson();
        PmtOrderMessage orderMessage = gson.fromJson(message, PmtOrderMessage.class);

        String paymentStatus = "";
        // insert save model

        try {
            PaymentIntent paymentIntent = createPaymentIntent(orderMessage);
            // PAID(6),
            paymentStatus = PaymentStatus.PAID.name();
            log.info("Payment request created");
        } catch (StripeException e) {
            log.error("Payment fail, errorMessage = {}", e.getMessage());
            // them trang thai payment: CANCELED(6),
            paymentStatus = PaymentStatus.CANCELED.name();
        } finally {
            // todo save payment request vao data
            // push message to order service
            System.out.println("Payment saved");
            CallBackMessage callBackMessage = CallBackMessage.builder()
                    .orderId(orderMessage.getOrderId())
                    .paymentStatus(paymentStatus)
                    .build();
            kafkaTemplate.send("checkout-order-call-back-topic", gson.toJson(callBackMessage));
        }

    }

    public PaymentIntent createPaymentIntent(PmtOrderMessage orderMessage) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", orderMessage.getAmount());
        params.put("currency", orderMessage.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
    }

    @Getter
    @Builder
    @AllArgsConstructor
    private static class CallBackMessage {
        private String orderId;
        private String paymentStatus;
    }
}
