package com.example.jpastudy.service;

import com.example.jpastudy.dto.ItemFormDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.ItemImage;
import com.example.jpastudy.repository.ItemImageRepository;
import com.example.jpastudy.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageService itemImageService;
    private final ItemImageRepository itemImageRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFileList) throws Exception{
        // 1. 상품 저장
        Item item= itemFormDto.createItem();
        itemRepository.save(item);

        // 2. 이미지 등록
        for(int i=0;i<itemImageFileList.size();i++){
            ItemImage itemImage=new ItemImage();
            itemImage.setItem(item);

            if(i==0) //대표 이미지로 설정
                itemImage.setRepImgYn("Y");
            else
                itemImage.setRepImgYn("N");

            itemImageService.saveItemImage(itemImage, itemImageFileList.get(i));
        }
        return item.getId();
    }
}
