package com.korit.silverbutton.service;

import com.korit.silverbutton.service.implement.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ProfileImgService {


    // 파일 업로드 로직
    public String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }

        // 파일명 고유화: UUID를 이용해 파일 이름을 고유하게 생성
        String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 저장할 경로
        String rootPath = "C:/silverButtonImg/upload/"; // 실제로는 외부 설정 파일로 분리 가능
        String filePath = "profile/" + newFileName;

        // 프로필 이미지를 저장할 디렉토리 생성
        File dir = new File(rootPath, "profile");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 실제 파일 저장 경로 설정
        Path uploadPath = Paths.get(rootPath + filePath);
        try {

            // 파일을 지정된 경로에 저장
            Files.write(uploadPath, file.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        // 저장된 파일 경로 반환
        return filePath;
    }

    // 이미지 삭제 로직
    public boolean deleteFile(String filePath) {
        // 삭제할 파일 경로
        String rootPath = "C:/silverButtonImg/upload/"; // 실제로는 외부 설정 파일로 분리 가능
        Path pathToFile = Paths.get(rootPath + filePath);

        // 파일이 존재하면 삭제
        try {
            File file = pathToFile.toFile();
            if (file.exists() && file.delete()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // 이미지 조회 로직 (파일 경로 반환)
    public String getProfileImg(String fileName) {
        // 파일 경로를 반환 (파일이 존재하는 경로로 리턴 가능)
        String rootPath = "C:/silverButtonImg/upload/"; // 실제로는 외부 설정 파일로 분리 가능
        String filePath = "profile/" + fileName;

        // 파일 경로 반환
        return filePath;
    }
}
