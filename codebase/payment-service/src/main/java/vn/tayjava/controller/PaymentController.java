package vn.tayjava.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

//    @PostMapping("/payment-intent")
//    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentInfoRequest paymentInfoRequest)
//            throws StripeException {
//
//        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentInfoRequest);
//        String paymentStr = paymentIntent.toJson();
//
//        return new ResponseEntity<>(paymentStr, HttpStatus.OK);
//    }

    @PostMapping("/payment-intent-2")
    public ResponseEntity<String> createPaymentIntent2(@RequestBody PaymentInfoRequest request)
            throws StripeException {

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(request.getAmount())
                .setCurrency(request.getCurrency())
//                .setPaymentMethod("card")
                .build();

        // tao payment intent
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // TODO gửi client key để xử lý thành toán
        if (paymentIntent != null) {
            // todo insert database
            return new ResponseEntity<>(paymentIntent.getClientSecret(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    @PostMapping
    public ResponseEntity processPayment(@RequestBody PaymentRequest paymentRequest) {
        // Logic xử lý thanh toán
        System.out.println("Payment Processed");
        return ResponseEntity.ok("Payment Processed");
    }

    @PostMapping("/compensate")
    public ResponseEntity refundPayment(@RequestBody PaymentRequest paymentRequest) {
        // Logic hoàn trả trong trường hợp cần bù trừ
        System.out.println("Payment Refunded");
        return ResponseEntity.ok("Payment Refunded");
    }
}
