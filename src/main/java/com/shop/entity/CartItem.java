package com.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name ="cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)   //하나의 장바구니 안에 여러개의 상품을 담을 수 있으니 다대일 관계로 매핑을 해줌 (상품[다] <-> 장바구니[일])
    @JoinColumn(name ="cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) //장바구니에 담을 상품의 정보를 알기위해 매핑 하나의 상품또한 여러 장바구니 상품으로 담김ㄹ 수 있으므로 다대일 매핑
    @JoinColumn( name = "item_id")
    private Item item;

    private int count; // 같은 상품을 몇개 담을 것인지 세는 변수

    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    public void addCount(int count){
        this.count += count;
    }

    public void updateCount(int count){this.count = count;}
}
