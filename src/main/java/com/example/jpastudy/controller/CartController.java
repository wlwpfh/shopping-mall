package com.example.jpastudy.controller;

import com.example.jpastudy.dto.CartDetailDto;
import com.example.jpastudy.dto.CartItemDto;
import com.example.jpastudy.entity.QCartItem;
import com.example.jpastudy.service.CartService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @PostMapping(value = "/cart")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid CartItemDto cartItemDto, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrorList) {
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        Long cartItemId;

        try {
            cartItemId = cartService.addCart(cartItemDto, email);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @GetMapping(value = "/cart")
    public String orderHistory(Principal principal, Model model){
        List<CartDetailDto> cartDetailDtoList=cartService.getCartList(principal.getName());

        model.addAttribute("cartItems", cartDetailDtoList);

        return "cart/cartList";
    }

    @PatchMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem (@PathVariable("cartItemId") Long cartItemId, int count, Principal principal){
        if(count<=0)
            return new ResponseEntity("최소 1개 이상 담아주세요.", HttpStatus.BAD_REQUEST);
        if(!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity("수정 권한이 없습니다.", HttpStatus.FORBIDDEN);

        cartService.updateCartItemCount(cartItemId, count);

        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/cartItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleeteCartItem (@PathVariable("cartItemId") Long cartItemId, Principal principal){
        if(!cartService.validateCartItem(cartItemId, principal.getName()))
            return new ResponseEntity("삭제 권한이 없습니다.", HttpStatus.FORBIDDEN);

        cartService.deleteCartItem(cartItemId);

        return new ResponseEntity(cartItemId, HttpStatus.OK);
    }

}
