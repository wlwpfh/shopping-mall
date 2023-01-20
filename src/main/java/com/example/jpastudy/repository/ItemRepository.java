package com.example.jpastudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.jpastudy.entity.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    //JpaRepository에서 제공하는 메소드
    //1. save - 엔티티 저장 및 수정
    //2. delete - 엔티티 삭제
    //3. count - 엔티티 총 개수 반환
    //4. findAll - 모든 엔티티 조회
}
