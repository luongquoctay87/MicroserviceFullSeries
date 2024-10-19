package vn.tayjava.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.ChargeCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import vn.tayjava.controller.PaymentInfoRequest;
import vn.tayjava.model.Payment;
import vn.tayjava.repository.PaymentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j(topic = "PAYMENT-SERVICE")
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, @Value("${stripe.apiKey}") String secretKey) {
        this.paymentRepository = paymentRepository;
        Stripe.apiKey = secretKey;
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


    public PaymentIntent createPaymentIntent(PaymentInfoRequest paymentInfoRequest) throws StripeException {
        List<String> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");

        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentInfoRequest.getAmount());
        params.put("currency", paymentInfoRequest.getCurrency());
        params.put("payment_method_types", paymentMethodTypes);

        return PaymentIntent.create(params);
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
    public void listenerMessage(String message) {
        System.out.println(10/0);
        log.info("checkoutInfo = {}", message);
    }

}
