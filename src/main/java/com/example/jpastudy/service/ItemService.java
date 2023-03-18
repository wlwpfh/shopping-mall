package com.example.jpastudy.service;

import com.example.jpastudy.dto.ItemFormDto;
import com.example.jpastudy.dto.ItemSearchDto;
import com.example.jpastudy.dto.MainItemDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final S3FileService fileService;

    public Long saveItem(ItemFormDto itemFormDto, MultipartFile file) throws Exception {
        // 1. 상품 저장
        Item item = itemFormDto.createItem();
        String imageUrl = fileService.uploadFile(file, file.getOriginalFilename());
        // 2. 이미지 등록
        item.setImageUrl(imageUrl);
        itemRepository.save(item);
        return item.getId();
    }

    @Transactional(readOnly = true)
    public ItemFormDto getItemDetail(Long itemId) {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        ItemFormDto itemFormDto = ItemFormDto.of(item);

        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, MultipartFile file) throws Exception {
        Item item = itemRepository.findById(itemFormDto.getId())
                .orElseThrow(EntityNotFoundException::new);

        String imageUrl = fileService.uploadFile(file, file.getOriginalFilename());

        itemFormDto.setImageUrl(imageUrl);
        item.updateItem(itemFormDto);

        return item.getId();
    }

    @Transactional(readOnly = true)
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getAdminItemPage(itemSearchDto, pageable);
    }

    @Transactional(readOnly = true)
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return itemRepository.getMainItemPage(itemSearchDto, pageable);
    }
}
