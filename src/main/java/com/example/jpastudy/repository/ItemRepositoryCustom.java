package com.example.jpastudy.repository;

import com.example.jpastudy.dto.ItemSearchDto;
import com.example.jpastudy.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
