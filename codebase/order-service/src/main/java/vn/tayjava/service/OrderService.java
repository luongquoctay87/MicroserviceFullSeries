package vn.tayjava.service;

import com.google.zxing.WriterException;
import vn.tayjava.controller.request.PlaceOrderRequest;

import java.awt.image.BufferedImage;

public interface OrderService {

    String addOrder(PlaceOrderRequest orderRequest);

    BufferedImage generateQRCodeImage(String qrcode) throws WriterException;

    BufferedImage generateBarCodeImage(String barCode) throws WriterException;

    String checkoutOrder(String orderId);
}
