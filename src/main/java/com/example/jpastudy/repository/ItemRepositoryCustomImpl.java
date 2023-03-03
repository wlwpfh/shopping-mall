package com.example.jpastudy.repository;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.dto.ItemSearchDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus== null? null: QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression registerDateAfter(String searchDateType){
        LocalDateTime dateTime= LocalDateTime.now();
        if(StringUtils.equals("all", searchDateType) || searchDateType==null)
            return null;
        else if(StringUtils.equals("1d", searchDateType))
            dateTime= dateTime.minusDays(1);
        else if(StringUtils.equals("1w", searchDateType))
            dateTime= dateTime.minusWeeks(1);
        else if(StringUtils.equals("1m", searchDateType))
            dateTime= dateTime.minusMonths(1);
        else if(StringUtils.equals("6m", searchDateType))
            dateTime= dateTime.minusMonths(6);

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){
        if(StringUtils.equals("itemName", searchBy))
            return QItem.item.itemName.like("%"+searchQuery+"%");
        if(StringUtils.equals("createdBy", searchBy))
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        return null;
    }
}
