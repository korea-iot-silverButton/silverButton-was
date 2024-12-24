package com.korit.silverbutton.service;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {


    ResponseDto<UserResponseDto> signInUser(String userId, String password);

    ResponseDto<UserResponseDto> getAllUsers(String userId);

    ResponseDto<UserResponseDto> updateUser(String userId, UserRequestDto dto);

    ResponseDto<Void> deleteUser(String userId);
}