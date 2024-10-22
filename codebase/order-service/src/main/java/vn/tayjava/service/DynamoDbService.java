//package vn.tayjava.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.awscore.exception.AwsServiceException;
//import software.amazon.awssdk.core.exception.SdkClientException;
//import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
//import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
//import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
//import vn.tayjava.controller.request.OrderItemRequest;
//import vn.tayjava.controller.request.PlaceOrderRequest;
//
//import java.time.Instant;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class DynamoDbService {
//
//    private final DynamoDbClient dynamoDbClient;
//    private final String tableName = "dev_order";
//
//    public void placeOrder(PlaceOrderRequest orderRequest) {
//        log.info("Place order request: {}", orderRequest);
//
//        Map<String, AttributeValue> order = new HashMap<>();
//        order.put("customer_id", AttributeValue.builder().n(orderRequest.getCustomerId().toString()).build());
//        order.put("created_at", AttributeValue.builder().s(String.valueOf(Instant.now().getNano())).build());
//        order.put("totalPrice", AttributeValue.builder().s(orderRequest.getAmount().toString()).build());
//
//        Map<String, AttributeValue> orderItems = new HashMap<>();
//        for (OrderItemRequest item : orderRequest.getOrderItems()) {
//            orderItems.put("product_id", AttributeValue.builder().n(item.getProductId().toString()).build());
//            orderItems.put("quantity", AttributeValue.builder().n(item.getQuantity().toString()).build());
//            orderItems.put("price", AttributeValue.builder().n(item.getPrice().toString()).build());
//            orderItems.put("unit", AttributeValue.builder().s(item.getUnit()).build());
//        }
//        order.put("orderItems", AttributeValue.builder().m(orderItems).build());
//
//        PutItemRequest request = PutItemRequest.builder()
//                .tableName(tableName)
//                .item(order)
//                .build();
//
//        try {
//            dynamoDbClient.putItem(request);
//        } catch (AwsServiceException e) {
//            throw new RuntimeException(e);
//        } catch (SdkClientException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
//
