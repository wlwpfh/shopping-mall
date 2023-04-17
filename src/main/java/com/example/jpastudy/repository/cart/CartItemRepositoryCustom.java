package com.example.jpastudy.repository.cart;

import com.example.jpastudy.dto.CartDetailDto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepositoryCustom {
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);
}
