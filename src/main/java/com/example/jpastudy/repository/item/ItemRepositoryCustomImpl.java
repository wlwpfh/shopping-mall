package com.example.jpastudy.repository.item;

import com.example.jpastudy.constant.ItemSellStatus;
import com.example.jpastudy.dto.ItemSearchDto;
import com.example.jpastudy.dto.MainItemDto;
import com.example.jpastudy.dto.QMainItemDto;
import com.example.jpastudy.entity.Item;
import com.example.jpastudy.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ItemRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus) {
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    private BooleanExpression registerDateAfter(String searchDateType) {
        LocalDateTime dateTime = LocalDateTime.now();
        if (StringUtils.equals("all", searchDateType) || searchDateType == null)
            return null;
        else if (StringUtils.equals("1d", searchDateType))
            dateTime = dateTime.minusDays(1);
        else if (StringUtils.equals("1w", searchDateType))
            dateTime = dateTime.minusWeeks(1);
        else if (StringUtils.equals("1m", searchDateType))
            dateTime = dateTime.minusMonths(1);
        else if (StringUtils.equals("6m", searchDateType))
            dateTime = dateTime.minusMonths(6);

        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if (StringUtils.equals("itemName", searchBy))
            return QItem.item.itemName.like("%" + searchQuery + "%");
        if (StringUtils.equals("createdBy", searchBy))
            return QItem.item.createdBy.like("%" + searchQuery + "%");
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        List<Item> results = queryFactory.selectFrom(QItem.item)
                .where(registerDateAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QItem.item)
                .where(registerDateAfter(itemSearchDto.getSearchDateType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression itemNameLike(String searchQuery) {
        return StringUtils.isEmpty(searchQuery) ? null : QItem.item.itemName.like("%" + searchQuery + "%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QItem item = QItem.item;

        List<MainItemDto> results = queryFactory.select(
                        new QMainItemDto(
                                item.id,
                                item.itemName,
                                item.itemDetail,
                                item.imageUrl,
                                item.price
                        )
                ).from(item)
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        long total = queryFactory.select(Wildcard.count)
                .from(item)
                .where(itemNameLike(itemSearchDto.getSearchQuery()))
                .fetchOne();


        return new PageImpl<>(results, pageable, total);
    }

    public List<Item> findByItemDetail(@Param("itemDetail")String itemDetail){
        List<Item> result=queryFactory.selectFrom(QItem.item)
                .where(QItem.item.itemDetail.contains(itemDetail))
                .orderBy(QItem.item.price.desc())
                .fetch();

        return result;
    }
}
