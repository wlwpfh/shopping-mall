package com.example.jpastudy.service;

import com.example.jpastudy.dto.OrderDto;
import com.example.jpastudy.dto.OrderItemDto;
import com.example.jpastudy.dto.OrderListDto;
import com.example.jpastudy.entity.Member;
import com.example.jpastudy.entity.Order;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.OrderItem;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.repository.OrderItemRepository;
import com.example.jpastudy.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public Long order(OrderDto orderDto, String email){
        Item item=itemRepository.findById(orderDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member=memberRepository.findByEmail(email);

        List<OrderItem> orderItemList=new ArrayList<>();
        OrderItem orderItem=OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        Order order=Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderListDto> getOrderList(String email, Pageable pageable){
        List<Order> orders= orderRepository.findOrders(email, pageable);
        Long totalCount=orderRepository.countOrder(email);

        List<OrderListDto> orderListDtos=new ArrayList<>();

        for(Order order: orders){
            OrderListDto orderListDto=new OrderListDto(order);
            List<OrderItem> orderItems=order.getOrderItems();
            OrderItem o=orderItemRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

            for(OrderItem orderItem: orderItems){
                OrderItemDto orderItemDto=new OrderItemDto(orderItem, o.getItem().getImageUrl());
                orderListDto.addOrderItemDto(orderItemDto);
            }
            orderListDtos.add(orderListDto);
        }
        return new PageImpl<OrderListDto>(orderListDtos, pageable, totalCount);
    }
}
