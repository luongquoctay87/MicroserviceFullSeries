package vn.tayjava.service;

import vn.tayjava.controller.request.PlaceOrderRequest;

public interface OrderService {

    String addOrder(PlaceOrderRequest orderRequest);

}
