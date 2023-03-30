package com.example.jpastudy.repository;

import com.example.jpastudy.dto.CartDetailDto;
import com.example.jpastudy.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.example.jpastudy.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, i.imageUrl)" +
        "from CartItem ci join ci.item i where ci.cart.id = :cartId and i.id = ci.item.id order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
