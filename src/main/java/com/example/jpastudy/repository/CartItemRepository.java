package com.example.jpastudy.repository;

import com.example.jpastudy.dto.CartDetailDto;
import com.example.jpastudy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.example.jpastudy.dto.CartDetailDto(ci.id, ci.item.itemName, ci.item.price, ci.count, ci.item.imageUrl) " +
            "from CartItem ci " +
            "where ci.cart.id = :cartId " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(@Param("cartId") Long cartId);

}
