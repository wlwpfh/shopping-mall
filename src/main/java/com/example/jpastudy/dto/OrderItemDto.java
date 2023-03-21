package com.example.jpastudy.dto;

import com.example.jpastudy.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    public OrderItemDto(OrderItem orderItem, String imageUrl){
        this.itemName=orderItem.getItem().getItemName();
        this.count=orderItem.getCount();
        this.orderPrice=orderItem.getOrderPrice();
        this.imageUrl=imageUrl;
    }

    private String itemName;
    private int count;
    private int orderPrice;
    private String imageUrl;
}
