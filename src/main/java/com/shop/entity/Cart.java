package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name ="cart")
@Getter
@Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column(name ="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) //이 어노테이션을 사용하여 회원 엔티티와 일대일 매핑
    @JoinColumn(name = "member_id") // 이 어노테이션으로 매핑할 외래키를 지정함. 즉 member엔티티의 기본키를 지정
    private Member member;

    //보면 회원 엔티티는 장바구니 엔티티와 관련된 소스가 포함되어있지 않지만 장바구니 엔티티는 일방적으로 회원 엔티티를 참조하고있다.
    // 이것이 1:1 단방향(장바구니 ->(참조) -> 회원) 매핑이다.
    // 이렇게 매핑을 맺으면 장바구니 엔티티를 조회하면서 회원 엔티티의 정보도 동시에 가져올 수 있는 장점이 있다.
    //우선 cart테이블을 생성하는 쿼리문이 먼저 생성된 후 외래키로 member_id를 지정하는 alter 쿼리문이 추가로 실행되는 순서를 알 수있다.

    public static Cart createCart(Member member){
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }
}
