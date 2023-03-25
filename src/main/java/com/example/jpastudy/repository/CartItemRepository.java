package com.example.jpastudy.repository;

import com.example.jpastudy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByItemIdAndCartId(Long cartId, Long itemId);
}