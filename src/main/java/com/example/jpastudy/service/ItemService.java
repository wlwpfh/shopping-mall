package com.example.jpastudy.service;

import com.example.jpastudy.dto.ItemFormDto;
import com.example.jpastudy.dto.ItemImageDto;
import com.example.jpastudy.dto.ItemSearchDto;
import com.example.jpastudy.dto.MainItemDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.ItemImage;
import com.example.jpastudy.repository.ItemImageRepository;
import com.example.jpastudy.repository.ItemRepository;
import groovy.util.logging.Log;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemImageService itemImageService;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageFileList) throws Exception {
        // 1. 상품 저장
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        // 2. 이미지 등록
        for (int i = 0; i < itemImageFileList.size(); i++) {
            ItemImage itemImage = new ItemImage();
            itemImage.setItem(item);

            if (i == 0) //대표 이미지로 설정
                itemImage.setRepImgYn("Y");
            else
                itemImage.setRepImgYn("N");

            itemImageService.saveItemImage(itemImage, itemImageFileList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId){
        List<ItemImage> itemImageList=itemImageRepository.findByItemIdOrderByItemIdAsc(itemId);
        List<ItemImageDto> itemImageDtoList = new ArrayList<>();

        for(ItemImage itemImage: itemImageList){
            ItemImageDto itemImageDto=ItemImageDto.of(itemImage);
            itemImageDtoList.add(itemImageDto);
        }

        Item item= itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto=ItemFormDto.of(item);

        itemFormDto.setItemImageDtoList(itemImageDtoList);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImageList) throws Exception{
        Item item=itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto);

        List<Long> itemImageIds=itemFormDto.getItemImageIds();

        for(int i=0;i<itemImageList.size();i++){
            itemImageService.updateItemImage(itemImageIds.get(i), itemImageList.get(i));
        }
        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly=true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
