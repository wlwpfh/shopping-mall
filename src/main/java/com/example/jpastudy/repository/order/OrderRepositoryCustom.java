package com.example.jpastudy.repository.order;

import com.example.jpastudy.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepositoryCustom {
    List<Order> findOrders(@Param("email") String email, Pageable pageable);
    Long countOrder(@Param("email") String email);
}
