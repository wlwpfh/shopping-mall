package com.example.jpastudy.controller;

import com.example.jpastudy.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/thymeleaf")
public class ThymeleafExController {
    @GetMapping(value="/ex1")
    String thymeleafEx(Model model){ //model을 이용해 뷰에 전달한 데이터를 key,value 구조로 넣어준다.
        model.addAttribute("data","타임리프 예제");
        return "thymeleafEx/ex1";
    }

    @GetMapping(value="/ex2")
    String thymeleafEx2(Model model){
        ItemDto itemDto=new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemName("테스트 상품 1");
        itemDto.setPrice(5000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/ex2";
    }

    @GetMapping(value="/ex3")
    String thymeleafEx3(Model model){
        List<ItemDto> itemDtoList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            ItemDto itemDto=new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemName("테스트 상품"+i);
            itemDto.setPrice(5000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/ex3";
    }
    @GetMapping(value="/ex4")
    String thymeleafEx4(Model model){
        List<ItemDto> itemDtoList=new ArrayList<>();
        for(int i=1;i<=10;i++){
            ItemDto itemDto=new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemName("테스트 상품"+i);
            itemDto.setPrice(5000*i);
            itemDto.setRegTime(LocalDateTime.now());
            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList",itemDtoList);
        return "thymeleafEx/ex4";
    }
    @GetMapping("/ex5")
    String thymeleafEx5(){
        return "thymeleafEx/ex5";
    }
}
