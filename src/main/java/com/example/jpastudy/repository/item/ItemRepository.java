package com.example.jpastudy.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jpastudy.entity.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    //JpaRepository에서 제공하는 메소드
    //1. save - 엔티티 저장 및 수정
    //2. delete - 엔티티 삭제
    //3. count - 엔티티 총 개수 반환
    //4. findAll - 모든 엔티티 조회
    List<Item> findByItemName(String itemName);

    List<Item> findByItemNameOrItemDetail(String itemName, String itemDetail);

    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPrice(Integer price);
}
