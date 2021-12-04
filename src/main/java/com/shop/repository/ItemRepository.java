package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

//2개의 제네릭 타입을 사용하는데 첫 번째에는 엔티티 타입 클래스, 두번째엔 기본키의 타입을 넣음 우리의 상품아이디는 Long형이여서 Long으로
//JPA Repository는 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의되어 있다.
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {
    List<Item> findByItemNm(String itemNm); //상품의 품번을 입력받아 조회하는 메서드

    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail); //상품의 품번 혹은 상세정보로 조회하기 위한 메서드

    List<Item> findByPriceLessThan(Integer price); //매개변수로 넘어온 가격보다 값이 작은 상품데이터를 조회하는 쿼리메소드

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price); // orderby + 속성명 + 정렬 방향을 통해 데이터의 순서 정리 쿼리 메서드

    //@Query 어노테이션 안에 JPQL로 작성한 쿼리문을 넣어줌으로 from 뒤에 엔티티 클래스로 작성한 Item을 지정하고 Item으로 부터 데이터를 select하겠다는 뜻
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")   //아래에 매개변수 itemDetail의 값이 %%사이에 들어가게 된다.
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail); //@Param 어노테이션을 이용하여서 매개변수로 온 상품정보를 JPQL에 들어갈 변수로 지정가능하다!

    @Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true) //nativeQuery 속성 사용하여 기존 DB의 쿼리내용을 가지고 조회
    List<Item> findByItemDetailByNative(@Param("itemDetail")String itemDetail);
}
