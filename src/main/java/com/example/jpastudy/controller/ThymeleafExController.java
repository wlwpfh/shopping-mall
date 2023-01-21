package com.example.jpastudy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value="/ex1")
    String thymeleafEx(Model model){ //model을 이용해 뷰에 전달한 데이터를 key,value 구조로 넣어준다.
        model.addAttribute("data","타임리프 예제");
        return "thymeleafEx/ex1";
    }
}
