package com.example.jpastudy.entity;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item=new Item();
        item.setItemName("테스트 상품");
        item.setItemDetail("테스트 상품 상세 설명");
        item.setPrice(1000);
        item.setStockNumber(100);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setUpdateTime(LocalDateTime.now());
        item.setRegTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    void cascadeTest(){
        Order order=new Order();

        for(int i=0;i<3;i++){
            Item item=this.createItem();
            itemRepository.save(item);

            OrderItem orderItem=new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder=orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }
}
