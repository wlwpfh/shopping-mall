package com.example.jpastudy.service;

import com.example.jpastudy.dto.CartDetailDto;
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
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Item item = itemRepository.findById(cartItemDto.getItemId())
                .orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        Cart cart = cartRepository.findByMemberId(member.getId());
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        if(savedCartItem != null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList= new ArrayList<>();

        Member member=memberRepository.findByEmail(email);
        Cart cart=cartRepository.findByMemberId(member.getId());

        if(cart==null){
            return cartDetailDtoList;
        }
        cartDetailDtoList=cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        Member member=memberRepository.findByEmail(email);

        CartItem cartItem=cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        Member saveMember=cartItem.getCart().getMember();

        if(!StringUtils.equals(member.getEmail(), saveMember.getEmail()))
            return false;
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem=cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);

        cartItem.updateCount(count);
    }
}
