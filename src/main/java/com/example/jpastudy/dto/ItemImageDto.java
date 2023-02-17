package com.example.jpastudy.dto;

import com.example.jpastudy.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImageDto {
    private Long id;
    private String imageName;
    private String originalImageName;
    private String imageUrl;
    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImageDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImageDto.class);
    }
}
