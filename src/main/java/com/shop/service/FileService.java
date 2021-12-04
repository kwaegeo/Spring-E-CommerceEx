package com.shop.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {  //파일을 처리하는 클래스 (업로드, 삭제 기능)

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID(); // 서로 다른 개체들을 구별하기 위해서 이름을 부여할 때 사용한다. 실제 사용시 중복 가능성이 별로 없으며 파일 이름으로 하며녀 중복문제해결가능
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension; //UUID로 받은 값이랑 파일 이름의 확장자를 조합하여 저장될 파일 이름을 만든다.
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl); //생성자로 파일이 저장될 위치와 파일의 이름을 넘겨서 파일에 쓸 파일 출력 스트림을 만든다.
        fos.write(fileData); //파일데이터를 파일 출력 스트림에 입력한다.
        fos.close();
        return savedFileName; //업로드 된 파일의 이름을 반환한다.
    }

    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); //파일이 저장된 경로를 이용해서 파일 객체를 생성하고

        if(deleteFile.exists()){ //해당 파일이 존재하면 그 파일을 삭제
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        }else{
            log.info("파일이 존재하지 않습니다.");
        }
    }
}
