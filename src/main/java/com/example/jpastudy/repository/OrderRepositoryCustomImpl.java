package com.example.jpastudy.repository;

import com.example.jpastudy.entity.Order;
import com.example.jpastudy.entity.QOrder;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{
    private JPAQueryFactory queryFactory;

    public OrderRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Order> findOrders(String email, Pageable pageable) {
        List<Order> result= queryFactory.selectFrom(QOrder.order)
                .where(QOrder.order.member.email.eq(email))
                .orderBy(QOrder.order.orderDate.desc())
                .fetch();
        return result;
    }

    @Override
    public Long countOrder(String email) {
        Long result=queryFactory.select(Wildcard.count)
                .from(QOrder.order)
                .where(QOrder.order.member.email.eq(email))
                .fetchOne();
        return result;
    }
}
