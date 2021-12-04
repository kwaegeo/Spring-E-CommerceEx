package com.shop.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class OrderItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name ="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)           //하나의 상품은 여러 주문 상품에 들어갈 수 있다. 다대일
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)          // 한번의 주문으로 여러개의 상품을 주문 가능하기 때문 다대일
    @JoinColumn(name ="order_id")
    private Order order;

    private int orderPrice; //주문 가격

    private int count; //수량

    //상품과 주문수량을 통해서 주문상품 객체를 만드는 메서드
    public static OrderItem createOrderItem(Item item, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);           //주문 상품과 수량을 세팅 후
        orderItem.setCount(count);
        orderItem.setOrderPrice(item.getPrice());  //가격도 세팅

        item.removeStock(count); //주문 수량만큼 상품의 재고 수량 감소시킨다.
        return orderItem;
    }

    public int getTotalPrice(){ //주문가격과 주문수량을 곱하여 총 가격을 계산하는 메서드
        return orderPrice*count;
    }

    public void cancle(){
        this.getItem().addStock(count);
    }
}
