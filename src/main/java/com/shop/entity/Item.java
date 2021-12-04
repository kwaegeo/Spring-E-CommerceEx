package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    //이 속성을 기본키로 설정합니다.
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 코드

    @Column(nullable = false, length =50)
    private String itemNm; //상품명

    @Column(name="price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber){  //상품 주문 시 상품의 재고를 감소시키는 메서드
        int restStock = this.stockNumber - stockNumber; //남은 재고수량을 먼저 계산

        if(restStock<0){ //재고가 없을경우
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " +this.stockNumber +")");
        }

        this.stockNumber = restStock; //남은 재고 수량을 현재 재고 값으로 할당
    }


    public void addStock(int stockNumber){ //주문 취소 시 주문 수량만큼 상품의 재고를 증가시키는 메서드
        this.stockNumber += stockNumber;
    }
}
