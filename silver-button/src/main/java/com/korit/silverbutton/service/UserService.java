package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    // 전체 user조회
    ResponseDto<List<UserResponseDto>> getAllUsers(String userId);

    // userId로 특정 user 조회
    ResponseDto<UserProfileDto> getUser(String userId);

    // 로그인 한 user의 정보 수정해야 하니까 프론트에서는 getUser로 내용 불러오고 이 내용으로
    // input 창 넣어서 수정 가능하도록
//    ResponseDto<UserProfileDto> updateUser(String userId, UserRequestDto dto);
//
//    // 사용자 정보 수정 및 업데이트
//    ResponseDto<UserProfileDto> updateUser(String userId, UpdateRequestDto dto) // http://localhost:4040/api/v1/manage/allusers 작동 함
//    ;

    ResponseDto<UserProfileDto> updatePassword(String userId, String currentPassword, String newPassword);

    // user 삭제
    ResponseDto<Void> deleteUser(String userId);

    ResponseDto<UserProfileDto> updateUser(String userId, UserProfileDto dto);
}