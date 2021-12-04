package com.shop.dto;


import com.shop.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto { //주문이력 조회 시 주문 데이터를 화면에 띄울 때 사용할 DTO 클래스

    private String itemNm; //상품명

    private int count; //주문 수량

    private int orderPrice; //주문 금액

    private String imgUrl;//상품 이미지 경로

    public OrderItemDto(OrderItem orderItem, String imgUrl){
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }
}
