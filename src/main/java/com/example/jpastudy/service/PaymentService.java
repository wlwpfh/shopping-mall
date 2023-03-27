package com.example.jpastudy.service;

import com.example.jpastudy.entity.OrderItem;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class PaymentService {

    public void makeVirtualPayment(OrderItem orderItem) throws IOException, InterruptedException {

        String amount = String.valueOf(orderItem.getTotalPrice());
        String orderId = orderItem.getId().toString();
        String orderName = orderItem.getItem().getItemName();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.tosspayments.com/v1/virtual-accounts"))
                    .header("Authorization", "Basic dGVzdF9za19PRVA1OUx5Ylo4QktvOVg5bWFCMzZHWW83cFJlOg==")
                    .header("Content-Type", "application/json")
                    .method("POST",
                            HttpRequest.BodyPublishers.ofString("{\"amount\":" + amount + ",\"orderId\":\"" + orderId
                                    + "\",\"orderName\":" + orderName + ",\"customerName\":\"박토스\",\"bank\":\"우리\"}"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
