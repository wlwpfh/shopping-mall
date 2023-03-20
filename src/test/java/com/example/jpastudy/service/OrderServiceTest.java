package com.example.jpastudy.service;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.constant.Role;
import com.example.jpastudy.dto.OrderDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.Member;
import com.example.jpastudy.entity.Order;
import com.example.jpastudy.entity.OrderItem;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.MemberRepository;
import com.example.jpastudy.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderServiceTest {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){
        Item item=new Item();
        item.setItemName("테스트 아이템");
        item.setPrice(2000);
        item.setItemDetail("테스트 아이템 상품 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member=new Member();
        member.setName("테스트 사용자");
        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item=saveItem();
        Member member=saveMember();

        OrderDto orderDto=new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId= orderService.order(orderDto, member.getEmail());

        Order order=orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        int totalPrice=orderDto.getCount()*item.getPrice();

        Assertions.assertEquals(totalPrice, order.getTotalPrice());
    }
}
