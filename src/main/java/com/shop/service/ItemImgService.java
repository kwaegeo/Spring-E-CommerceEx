package com.shop.service;


import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService { //상품 이미지를 업로드하고, 상품 이미지 정보를 저장하는 서비스

    @Value("${itemImgLocation}")  //설정 파일에 등록한 경로 값을 불러와서 변수에 저장해준다.
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if(!StringUtils.isEmpty(oriImgName)){            //사용자가 상품의 이미지를 등록했다면 (경로,파일이름,배열)을 매개변수로 uploadfile 메소드를 호출
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //호출결과로 오는 로컬에 저장된 파일의 이름을 imgName변수에 저장한다.
            imgUrl = "/images/item/" + imgName; //저장한 상품 이미지를 불러올 경로를 설정한다.
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
        itemImgRepository.save(itemImg); //입력받은 상품 이미지의 정보를 저장한다.
    }

    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
        if(!itemImgFile.isEmpty()){ //상품 이미지를 수정한 경우에 상품이미지 업데이트
            ItemImg savedItemImg = itemImgRepository.findById(itemImgId).orElseThrow(EntityNotFoundException::new); //아이디를 가지고 기존 저장한 상품 이미지 엔티티 조회

            //기존 이미지 파일 삭제
            if(!StringUtils.isEmpty(savedItemImg.getImgName())){ //기존에 등록된 상품이미지 파일이 있다면?
                fileService.deleteFile(itemImgLocation+"/"+savedItemImg.getImgName()); // 해당 파일 삭제
            }
            String orImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, orImgName, itemImgFile.getBytes()); //업데이트를 한 상품 이미지 파일 업로드
            String imgUrl = "/images/item/" + imgName;
            savedItemImg.updateItemImg(orImgName,imgName,imgUrl); // 변경된 상품 이미지 정보를 세팅해준다.

        }


    }

}
