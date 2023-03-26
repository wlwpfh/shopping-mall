package com.example.jpastudy.service;

import com.example.jpastudy.entity.Cart;
import com.example.jpastudy.entity.CartItem;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.dto.CartItemDto;
import com.example.jpastudy.entity.Member;
import com.example.jpastudy.repository.CartItemRepository;
import com.example.jpastudy.repository.CartRepository;
import com.example.jpastudy.repository.ItemRepository;
import com.example.jpastudy.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email){
        Item item= itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);

        Member member=memberRepository.findByEmail(email);

        Cart cart=cartRepository.findByMemberId(member.getId());

        if(cart==null){
            cart=Cart.createCart(member);
            cartRepository.save(cart);
        }
        CartItem saveCartItem=cartItemRepository.findByItemIdAndCartId(cart.getId(), item.getId());

        if(saveCartItem!=null){
            saveCartItem.addCount(cartItemDto.getCount());
            return saveCartItem.getId();
        }
        CartItem cartItem=CartItem.createCartItem(cart, item, cartItemDto.getCount());
        cartItemRepository.save(cartItem);

        return cartItem.getId();
    }
}
