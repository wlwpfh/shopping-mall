package com.example.jpastudy.dto;

import com.example.jpastudy.entity.CartItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartDetailDto  {
    private Long cartItemId;

    private String itemName;

    private int price;

    private int count;

    private String imageUrl;

    public CartDetailDto(Long cartItemId, String itemName, int price, int count, String imageUrl){
        this.cartItemId=cartItemId;
        this.itemName=itemName;
        this.price=price;
        this.count=count;
        this.imageUrl=imageUrl;
    }

    public CartDetailDto(CartItem cartItem){
        this.cartItemId=cartItem.getId();
        this.itemName=cartItem.getItem().getItemName();
        this.price=cartItem.getItem().getPrice();
        this.count=cartItem.getCount();
        this.imageUrl=cartItem.getItem().getImageUrl();
    }
}
