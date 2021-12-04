package com.shop.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.dto.QMainItemDto;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;

import com.shop.entity.QItemImg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class ItemRepositoryCustomImpl implements ItemRepositoryCustom {

    private JPAQueryFactory queryFactory; //동적인 쿼리를 생성하기 위해서 JPAQueryFactory 클래스를 사용

    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em); //query의 생성자로 entitymanager 객체를 넣어준다.
    }

    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){ //상품 판매조건이 전체인 경우 null을 리턴
        return searchSellStatus == null? null: QItem.item.itemSellStatus.eq(searchSellStatus); //null이 아니라 조건이 있으면 해당 조건의 상품만 조회하도록
    }

    private BooleanExpression regDtsAfter(String searchDateType){ //searchDateType의 값에 따라서 값을 이전 시간의 값으로 세팅 후 이후 등록된 상품만 등록된 상품만 조회
        LocalDateTime dateTime = LocalDateTime.now();

        if(StringUtils.equals("all", searchDateType) || searchDateType == null){
            return null;
        }else if(StringUtils.equals("1d",searchDateType)){
            dateTime = dateTime.minusDays(1);
        }else if(StringUtils.equals("1w",searchDateType)){
            dateTime = dateTime.minusWeeks(1);
        }else if(StringUtils.equals("1m",searchDateType)){
            dateTime = dateTime.minusMonths(1);
        }else if(StringUtils.equals("6m",searchDateType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    private BooleanExpression searchByLike(String searchBy, String searchQuery){ //searchBy 값에 따라서 상품명에 검색어를 포함하거 있는 상품 또는 상품생성자의 아이디를 포함한 상품을 조회하도록 한다.

        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        }else if(StringUtils.equals("createdBy",searchBy)){
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QueryResults<Item> results = queryFactory                    //queryFactory를 이용해서 쿼리를 생성한다.
                .selectFrom(QItem.item) //상품 데이터를 조회하기 위해 QItem의 item을 지정하고
                .where(regDtsAfter(itemSearchDto.getSearchDateType()), //메서드의 반환값들을 넣어서 각 조건들을 만들어준다.
                    searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                    searchByLike(itemSearchDto.getSearchBy(),
                    itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc()) //정렬은 id값의 내림차순으로
                .offset(pageable.getOffset()) //데이터를 가지고 올 시작 인덱스를 정한다.
                .limit(pageable.getPageSize()) //한번에 가지고 올 최대 개수를 지정
                .fetchResults(); //조회한 리스트 및 전체 개수를 포함하는 QueryResults를 반환한다. 상품 데이터 리스트와 상품데이터 전체 개수를 조회하는 쿼리문 = 2번실행된다.

        List<Item> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total); //조회한 데이터를 Page클래스의 구현체인 PageImpl객체로 반환한다.
    }

    private BooleanExpression itemNmLike(String searchQuery){
        return StringUtils.isEmpty(searchQuery) ? null:QItem.item.itemNm.like("%"+searchQuery+"%");
    }

    @Override
    public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
        QItem item = QItem.item;
        QItemImg itemImg = QItemImg.itemImg;

        QueryResults<MainItemDto> results = queryFactory
                .select(
                        new QMainItemDto(
                                item.id,
                                item.itemNm,
                                item.itemDetail,
                                itemImg.imgUrl,
                                item.price)
                        )
                .from(itemImg)
                .join(itemImg.item, item)
                .where(itemImg.repimgYn.eq("Y"))
                .where(itemNmLike(itemSearchDto.getSearchQuery()))
                .orderBy(item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<MainItemDto> content = results.getResults();
        long total = results.getTotal();
        return new PageImpl<>(content, pageable, total);
    }

}
