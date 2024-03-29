package com.example.jpastudy.repository.cart;

import com.example.jpastudy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long>, CartItemRepositoryCustom {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

}
