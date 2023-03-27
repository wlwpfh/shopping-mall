package com.example.jpastudy.service;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.dto.CartItemDto;
import com.example.jpastudy.entity.CartItem;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.Member;
import com.example.jpastudy.repository.CartItemRepository;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Assertions;

import javax.persistence.EntityNotFoundException;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartServiceTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartService cartService;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemName("테스트 아이템");
        item.setPrice(2000);
        item.setItemDetail("테스트 아이템 상품 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);

        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setName("테스트 사용자");
        member.setEmail("test@test.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addCart(){
        Item item=saveItem();
        Member member=saveMember();

        CartItemDto cartItemDto=new CartItemDto();
        cartItemDto.setCount(5);
        cartItemDto.setItemId(item.getId());

        Long cartItemId= cartService.addCart(cartItemDto, member.getEmail());
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(item.getId(), cartItem.getItem().getId());
        Assertions.assertEquals(cartItemDto.getCount(), cartItem.getCount());
    }
}
