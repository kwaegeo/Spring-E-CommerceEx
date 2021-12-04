package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;


@Getter
@Setter
public class ItemImgDto {  //상품 저장 후 상품 이미지에 대한 데이터를 전달할 DTO클래스

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); //modelmapper 객체를 추가하고

    public static ItemImgDto of(ItemImg itemImg){                //엔티티 객체를 파라미터로 받아서 itemimg 객체의 자료형이랑 멤버변수가 itemimgDto랑같으면 itemimgdto로 값을 복사해서 반환한다.
        return modelMapper.map(itemImg, ItemImgDto.class);
    }
}
