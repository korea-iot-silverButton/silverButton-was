package com.korit.silverbutton.service;

import com.korit.silverbutton.common.constant.ResponseMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProfileImgService {

    @Value("${user.dir}") // 실버버튼 프로젝트 로컬 환경
    private String rootPath;

    // 파일 업로드 로직
    public String uploadFile(MultipartFile file) {
        if (file == null) {
            return null;
        }

        // 파일명 고유화: UUID를 이용해 파일 이름을 고유하게 생성
        String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 프로필 이미지 저장 디렉토리 경로
        Path profileDir = Paths.get(rootPath, "profile");

        // 프로필 이미지를 저장할 디렉토리 생성
        try {
            if (Files.notExists(profileDir)) {
                Files.createDirectories(profileDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 실제 파일 저장 경로 설정
        Path filePath = profileDir.resolve(newFileName);

        try {
            // 파일을 지정된 경로에 저장
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // 저장된 파일 경로 반환
        return Paths.get("profile", newFileName).toString(); // 상대 경로 반환
    }

    // 여러 파일 업로드 처리 (board 디렉토리로 저장)
    public List<String> uploadFiles(List<MultipartFile> files) {
        List<String> filePaths = new ArrayList<>();

        // rootPath가 null이거나 비어 있지 않은지 확인
        if (rootPath == null || rootPath.isEmpty()) {
            System.out.println("Root path is not set properly.");
            return filePaths;
        }
        System.out.println("Root path: " + rootPath);
        for (MultipartFile file : files) {
            if (file == null || file.isEmpty()) {
                System.out.println(ResponseMessage.NO_FILE_TO_UPLOAD); // 파일이 없을 경우 안내 메시지
                continue;
            }

            // 파일명 고유화: UUID를 이용해 파일 이름을 고유하게 생성
            String newFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            System.out.println("새로운 파일명: " + newFileName);
            // 저장할 경로 (board 디렉토리)
            String filePath = rootPath + "/board/";
            System.out.println("저장할 경로: " + filePath);
            System.out.println("저장할 rootPath 경로: " + rootPath);

            // 디렉토리 생성 여부 확인
            File dir = new File(filePath);
            if (!dir.exists()) {
                boolean dirCreated = dir.mkdirs();
                if (dirCreated) {
                    System.out.println(ResponseMessage.DIRECTORY_CREATED_SUCCESS + filePath);
                } else {
                    System.out.println(ResponseMessage.FAILED_TO_CREATE_DIRECTORY + filePath);
                }
            } else {
                System.out.println(ResponseMessage.DIRECTORY_ALREADY_EXISTS + filePath);
            }

            // 파일 시스템 권한 확인
            if (!dir.canWrite()) {
                System.out.println(ResponseMessage.NO_WRITE_PERMISSION + filePath);
            }

            // 실제 파일 저장 경로 설정
            Path uploadPath = Paths.get(filePath + newFileName).toAbsolutePath();
            System.out.println(ResponseMessage.SAVING_FILE_TO + uploadPath);
            System.out.println("파일 저장 경로: " + uploadPath);

            try {
                // 파일을 지정된 경로에 저장
                Files.write(uploadPath, file.getBytes());
                System.out.println(ResponseMessage.FILE_UPLOADED_SUCCESS + uploadPath);

                filePaths.add(filePath + newFileName); // 업로드된 파일 경로를 리스트에 추가
            } catch (Exception e) {
                System.out.println(ResponseMessage.FAILED_TO_UPLOAD_FILE + file.getOriginalFilename());
                e.printStackTrace();
            }
        }

        // 업로드된 파일 경로 리스트 출력
        if (filePaths.isEmpty()) {
            System.out.println(ResponseMessage.NO_FILES_UPLOADED);
        } else {
            System.out.println(ResponseMessage.UPLOADED_FILE_PATHS + filePaths);
        }

        return filePaths;
    }


    // 이미지 삭제 로직
    public boolean deleteFile(String filePath) {
        // 삭제할 파일 경로
        Path pathToFile = Paths.get( filePath).normalize(); // normalize()로 중복된 경로 해결

        if (!filePath.startsWith(rootPath)) {
            pathToFile = Paths.get(rootPath, filePath).normalize();
        }
        // 파일이 존재하면 삭제
        try {
            File file = pathToFile.toFile();
            if (file.exists() && file.delete()) {
                System.out.println(ResponseMessage.FILE_DELETION_SUCCESS); // 삭제 성공 메시지 출력
                return true;
            } else {
                System.out.println(ResponseMessage.FILE_NOT_FOUND + filePath); // 파일이 없을 경우 메시지 출력
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ResponseMessage.FILE_DELETION_FAIL); // 삭제 실패 메시지 출력
        }

        return false;
    }

    // 이미지 조회 로직 (파일 경로 반환)
    public String getProfileImg(String fileName) {
        // 파일 경로를 반환 (파일이 존재하는 경로로 리턴 가능)

        return Paths.get("profile", fileName).toString();
    }
}
