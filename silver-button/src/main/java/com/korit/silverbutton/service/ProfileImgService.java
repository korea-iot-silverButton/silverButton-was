package com.korit.silverbutton.service;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.UserRepository;
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

    private final Storage storage;
    private final UserRepository userRepository;
    @Value("${user.dir}") // 실버버튼 프로젝트 로컬 환경
    private String rootPath;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public ProfileImgService(Storage storage, UserRepository userRepository) {
        this.storage = storage;
        this.userRepository = userRepository;
    }

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





    public ResponseDto<List<String>> uploadFiles(List<MultipartFile> files) {
        List<String> uploadedFileUrls = new ArrayList<>();

        try {
            for (MultipartFile file : files) {
                // 새로 업로드할 파일 이름(UUID) 생성
                String uuid = UUID.randomUUID().toString();
                String ext = file.getContentType();

                // Google Cloud Storage에 새 파일 업로드
                BlobInfo blobInfo = storage.create(
                        BlobInfo.newBuilder(bucketName, uuid)  // bucketName 사용
                                .setContentType(ext)
                                .build(),
                        file.getInputStream()
                );

                // 업로드된 파일의 URL 생성
                String fileUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uuid);  // bucketName 사용
                uploadedFileUrls.add(fileUrl);
            }

            return ResponseDto.setSuccess("PROFILE_IMG_UPLOAD_SUCCESS", uploadedFileUrls);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_UPDATE_FAIL");
        }
    }

    // 클라우드 파일 삭제 (private로 유지)
    private boolean deleteFileFromCloudStorage(String filePath) {
        try {
            String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
            storage.delete(bucketName, fileName);  // Cloud Storage에서 파일 삭제
            System.out.println(ResponseMessage.FILE_DELETION_SUCCESS); // 삭제 성공 메시지 출력
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(ResponseMessage.FILE_DELETION_FAIL); // 삭제 실패 메시지 출력
        }

        return false;
    }


    // 이미지 삭제 로직
    public boolean deleteFile(String filePath) {
        // 파일이 Cloud Storage에 저장된 경우 삭제
        if (filePath.startsWith("https://storage.googleapis.com")) {
            return deleteFileFromCloudStorage(filePath);  // 클라우드에서 삭제
        } else {
            // 로컬 파일 삭제
            Path pathToFile = Paths.get(filePath).normalize(); // normalize()로 중복된 경로 해결
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
        }

        return false;
    }


    // 이미지 조회 로직 (파일 경로 반환)
    public String getProfileImg(String fileName) {
        // 파일 경로를 반환 (파일이 존재하는 경로로 리턴 가능)

        return Paths.get("profile", fileName).toString();
    }
}
