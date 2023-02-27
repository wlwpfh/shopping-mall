package com.example.jpastudy.repository;

import com.example.jpastudy.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {
    List<ItemImage> findByItemIdOrderByItemIdAsc(@Param("id") Long itemId);
}
