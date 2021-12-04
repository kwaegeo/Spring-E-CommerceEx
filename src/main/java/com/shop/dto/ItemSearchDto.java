package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; //현재 시간과 상품 등록일을 비교해서 상품데이터를 조회
                                   // -all, -1d, -1w, -1m, -6m  (전체, 1일, 1주일, 1달, 6달 순서)

    private ItemSellStatus searchSellStatus; //상품 판매상태 기준으로 조회

    private String searchBy; //어떤 유형으로 조회할지 선택 (상품명, 상품 등록자 아이디)

    private String searchQuery = ""; // 조회할 검색어를 저장할 변수 searchBy가 itemNm이면 상품명, createdBy면 등록자아이디 기준 검색

}
