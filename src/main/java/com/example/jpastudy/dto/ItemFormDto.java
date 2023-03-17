package com.example.jpastudy.dto;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {
    private Long id;

    @NotBlank(message=" 상품명은 필수 입력 값입니다.")
    private String itemName;

    @NotNull
    private Integer price;

    @NotBlank(message = "상세는 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;
    private String imageUrl;

    private static ModelMapper modelMapper=new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }
}
