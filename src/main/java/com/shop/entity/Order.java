package com.shop.entity;


import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //정렬 시 사용하는 order 키워드가 있기때문에 orders로 지정
@Getter
@Setter
public class Order extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name ="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="member_id")
    private Member member; //한명의 회원은 여러 번 주문을 할 수 있으니 주문 엔티티 기준에서 다대일 매핑을한다.

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문 상태

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch=FetchType.LAZY)  //추가로 부모의 영속성 상태 변화를 자식 엔티티에 모두 전이한다.
    private List<OrderItem> orderItems = new ArrayList<>();                //주문 엔티티에서 주문상품엔티티를 삭제했을때 같이 삭제되도록
      // 잘보면 order_id가 기본키인데 이것을 외래키로 가진 것은 OrderItem 테이블임 즉 연관 관계의 주인은 외래키를 가지고 있는 OrderItem이며 Order엔티티는 주인이 아니므로
      // mappedBy 속성으로 연관관계의 주인을 설정하는데 속성의 값인 order는 OrderItem에 있는 order에 의해 관리된다는 의미임.
      // 하나의 주문이 여러개의 주문 상품을 갖기 때문에 List 자료형을 사용해서 매핑을 한다.

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);     //주문상품 정보를 리스트에 담아준다.
        orderItem.setOrder(this); //order엔티티와 orderItem 엔티티가 양방향 참조이므로 서로 세팅해준다.
    }

    public static Order createOrder(Member member, List<OrderItem> orderItemList){
        Order order = new Order();
        order.setMember(member); //주문에 회원 정보를 세팅해주고
        for(OrderItem orderItem : orderItemList){ // 상품 페이지에선 1개의 상품을 주문하지만, 장바구니 페이지에선 한번에 여러개의 상품을 주문할 수 있다. 따라서 여러개의 주문상품을 담을 수 있도록 객체를 추가
            order.addOrderItem(orderItem);
        }
        order.setOrderStatus(OrderStatus.ORDER); //주문 상태를 order로 세팅
        order.setOrderDate(LocalDateTime.now()); // 현재 시간을 주문 시간으로 세팅
        return order;
    }

    //총 주문 금액을 구하는 메서드
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    public void cancelOrder(){
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems){
            orderItem.cancle();
        }
    }
}
