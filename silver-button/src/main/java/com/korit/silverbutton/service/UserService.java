package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface UserService {
    // 전체 user조회
    ResponseDto<List<UserResponseDto>> getAllUsers(String userId);

    // userId로 특정 user 조회
    ResponseDto<UserProfileDto> getUser(String userId);

    ResponseDto<UserProfileDto> updatePassword(String userId, String currentPassword, String newPassword);

    boolean verifyPassword(String userId, String currentPassword);


    // user 삭제
    ResponseDto<Void> deleteUser(String userId);

    // 사용자 정보 수정 및 업데이트
    ResponseDto<UserProfileDto> updateUser(String userId, UserProfileDto dto);

    // 프로필 이미지 업로드
    ResponseDto<String> uploadFile(String userId, MultipartFile file);

    // 프로필 이미지 삭제
    ResponseDto<Void> deleteFile(String filePath);

    // 프로필 이미지 조회
    ResponseDto<String> getProfileImg(String userId);
}