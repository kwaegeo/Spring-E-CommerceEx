package com.shop.controller;

import com.shop.dto.ItemDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value="/thymeleaf")   //이 경로의 요청 들어오면 이 컨트롤러가 실행된다.
public class ThymeleafExController {

    @GetMapping(value = "/ex01")
    public String thymeleafExample01(Model model){
        model.addAttribute("data", "타임리프 예제입니다."); // 모델 객체를 이용해서 key value구조로 데이터 삽입
        return "thymeleafEx/thymeleafEx01"; //- templates 폴더 기준으로 뷰의 위치와 이름을 반환한다.
    }

    @GetMapping(value = "/ex02")
    public String thymeleafExample02(Model model){
        ItemDto itemDto = new ItemDto();
        itemDto.setItemDetail("상품 상세 설명");
        itemDto.setItemNm("테스트 상품1");
        itemDto.setPrice(10000);
        itemDto.setRegTime(LocalDateTime.now());

        model.addAttribute("itemDto", itemDto);
        return "thymeleafEx/thymeleafEx02";
    }

    @GetMapping(value = "/ex03")
    public String thymeleafExample03(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명"+i);
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList); //이 속성 이름으로 명시하여 뷰에선 이 이름속성을 이용해서 사용
        return "thymeleafEx/thymeleafEx03"; //model을  이 view로 전달한다.
    }

    //짝수 홀수 조건을 걸어 view를 처리하기 위한 예제 컨트롤러
    @GetMapping(value="/ex04")
    public String thymeleafExample04(Model model){
        List<ItemDto> itemDtoList = new ArrayList<>();

        for(int i=1; i<=10; i++){
            ItemDto itemDto = new ItemDto();
            itemDto.setItemDetail("상품 상세 설명");
            itemDto.setItemNm("테스트 상품"+i);
            itemDto.setPrice(1000*i);
            itemDto.setRegTime(LocalDateTime.now());

            itemDtoList.add(itemDto);
        }
        model.addAttribute("itemDtoList", itemDtoList);
        return "thymeleafEx/thymeleafEx04";
    }

    //링크 처리하기 위한 예제 컨트롤러
    @GetMapping(value = "/ex05")
    public String thymeleafExample05(){
        return "thymeleafEx/thymeleafEx05";
    }

    //다른 페이지에서 해당 링크를 호출할경우에 매개변수를 보면 들어오는 값이 param1,2가 같이 들어옴 모델에 해당 값을 추가하고 리턴
    @GetMapping(value = "/ex06")
    public String thymeleafExample06(String param1, String param2, Model model){
        model.addAttribute("param1", param1);
        model.addAttribute("param2", param2);
        return "thymeleafEx/thymeleafEx06";
    }

    @GetMapping(value="/ex07")
    public String thymeleafExample07(){
        return "thymeleafEx/thymeleafEx07";
    }

}
