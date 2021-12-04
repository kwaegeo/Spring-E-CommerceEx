package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping(value= "/admin/item/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }

    @PostMapping(value ="/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList){

        if(bindingResult.hasErrors()){ //상품 등록 시 필수 값이 없으면 다시 돌려보냄
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){ //상품 등록 시 첫번째 이미지가 없을 시 에러메시지와 같이 돌려보냄.
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }
        try{
            itemService.saveItem(itemFormDto, itemImgFileList); //상품 저장 로직을 호출
        } catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/"; //정상 업데이트가 되었으면 메인페이지로 이동시킴
    }

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model){

        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto",itemFormDto);
        }catch (EntityNotFoundException e){
            model.addAttribute("errorMessage","존재하지 않는 상품 입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }
        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList, Model model){

        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }
        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null){
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값입니다.");
            return "item/itemForm";
        }

        try {
            itemService.updateItem(itemFormDto, itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) //URL에 페이지가 있는경우와 없는 경우 2가지를 매핑함
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get():0,3); //페이징 처리를 위해서 PageRequest.of 메소드를 통해서 Pageable객체를 생성하고 첫번째파라미터는 조회할 페이지 번호이고 두번째는 한번에 가져올 데이터 수를 넣어준다.
                                                                                //URL 경로에 페이지 번호가 있으면 해당 페이지를 조회하도록 세팅, 번호가 없으면 0번 페이지를 조회하도록함.
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); //조회조건과 페이징정보가 담긴 매개변수를 넘겨 Page<item> 객체를 반환받은 뒤
        model.addAttribute("items",items); //조회 상품 데이터를 model객체에 넣어 뷰에 전달
        model.addAttribute("itemSearchDto",itemSearchDto); //기존 검색 조건을 유지한 채 이동할 수 있도록 뷰에 다시 검색조건을 넣어준다.
        model.addAttribute("maxPage",5); //하단에 표시될 페이지 번호의 최대 개수
        return "item/itemMng";
    }

    @GetMapping(value="/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item",itemFormDto);
        return "item/itemDtl";
    }
}
