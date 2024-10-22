//package vn.tayjava.controller;
//
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.tayjava.controller.request.PlaceOrderRequest;
//import vn.tayjava.service.DynamoDbService;
//
//@Validated
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/dynamodb")
//@Slf4j(topic = "DYNAMO-ORDER-CONTROLLER")
//public class DynamoOrderController {
//
//    private final DynamoDbService dynamoDbService;
//
//    @PostMapping("/place-order")
//    public void addOrder(@RequestBody PlaceOrderRequest orderRequest) {
//        dynamoDbService.placeOrder(orderRequest);
//    }
//}
