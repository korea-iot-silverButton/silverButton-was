package com.korit.silverbutton.service.implement;


import com.cloudinary.Cloudinary;
import com.korit.silverbutton.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class ImageServiceImpl implements ImageService {
    private final Cloudinary cloudinary;

    public ImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 업로드된 파일을 Cloudinary에 전송
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), Map.of());
            // 업로드된 이미지의 URL을 반환
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            // 예외 처리: 더 구체적인 메시지로 예외를 던짐
            throw new RuntimeException("이미지 업로드 실패: " + e.getMessage(), e);
        }
    }
}