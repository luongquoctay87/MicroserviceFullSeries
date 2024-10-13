package vn.tayjava.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.service.PaymentService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
@Slf4j(topic = "PAYMENT-CONTROLLER")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public String charge(@RequestBody Map<String, Object> data) throws StripeException {
        log.info("Charge request received");

        String token = (String) data.get("token");
        double amount = Double.parseDouble(data.get("amount").toString());

        // Call Stripe service to charge the card
        Charge charge = paymentService.charge(token, amount);

        return charge.getStatus(); // Return the payment status
    }

    @PostMapping("/payment-intent-test")
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
            throws StripeException {

        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
        String paymentStr = paymentIntent.toJson();

        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
    }

    @PutMapping("/payment-complete")
    public ResponseEntity<String> stripePaymentComplete(@RequestHeader(value="Authorization") String token)
            throws Exception {
//        String userEmail = ExtractJWT.payloadJWTExtraction(token, "\"sub\"");
//        if (userEmail == null) {
//            throw new Exception("User email is missing");
//        }
        return paymentService.stripePayment("orderId");
    }
}
