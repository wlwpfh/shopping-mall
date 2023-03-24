package com.example.jpastudy.entity;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.dto.ItemFormDto;
import com.example.jpastudy.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Entity
@Table(name = "item")
public class Item extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    private String imageUrl;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.imageUrl=itemFormDto.getImageUrl();
    }

    public void removeStock(int stockNumber){
        int restStock=this.stockNumber-stockNumber;
        if(restStock<0)
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 수량:"+this.stockNumber+")");
        this.stockNumber=restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber+=stockNumber;
    }
}
