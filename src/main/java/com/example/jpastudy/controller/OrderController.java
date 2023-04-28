package com.example.jpastudy.controller;


import com.example.jpastudy.dto.OrderDto;
import com.example.jpastudy.dto.OrderListDto;
import com.example.jpastudy.dto.pay.KakaoApproveResponse;
import com.example.jpastudy.service.KakaoPayService;
import com.example.jpastudy.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/order")
    public ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult, Principal principal, Model model) {
        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        String redirectUrl;
        try {
            redirectUrl = orderService.orderByPay(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(redirectUrl);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);

        Page<OrderListDto> orderListDtos = orderService.getOrderList(principal.getName(), pageable);

        model.addAttribute("orders", orderListDtos);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity(orderId, HttpStatus.OK);
    }

    @GetMapping("/order")
    public String approvePayment(@RequestParam("pg_token") String pgToken) {
        KakaoApproveResponse kakaoApproveResponse = orderService.completeOrderByPay(pgToken);
        return "order/orderHist";
    }

}
