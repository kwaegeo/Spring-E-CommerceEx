package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto { //상품 데이터 정보를 전달하는 DTO 클래스

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer StockNumber;

    private ItemSellStatus itemSellStatus; //아이템 판매 상태 (판매 / 품절)

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 저장 후 수정할 때 상품 이미지정보를 저장하는 리스트

    private List<Long> itemImgIds = new ArrayList<>(); //상품의 이미지 아이디를 저장하는 리스트로 수정 시에 이미지 아이디를 담아둘 용도

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class);
    }

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);
    }

}
