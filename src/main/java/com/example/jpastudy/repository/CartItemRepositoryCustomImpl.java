package com.example.jpastudy.repository;

import com.example.jpastudy.dto.CartDetailDto;
import com.example.jpastudy.entity.CartItem;
import com.example.jpastudy.entity.QCartItem;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class CartItemRepositoryCustomImpl implements CartItemRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public CartItemRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<CartDetailDto> findCartDetailDtoList(Long cartId) {
        List<CartItem> cartDetailDtoList = queryFactory.selectFrom(QCartItem.cartItem)
                .where(QCartItem.cartItem.cart.id.eq(cartId))
                .orderBy(QCartItem.cartItem.regTime.desc())
                .fetch();

        List<CartDetailDto> result = cartDetailDtoList.stream().map(CartDetailDto::new).collect(Collectors.toList());

        return result;
    }

}
