package com.korit.silverbutton.controller;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final @Lazy UserService userService;

    public final String USER_GET_USER_EMAIL = "/";

    @PostMapping("api/v1/signin/users")
    public ResponseEntity<ResponseDto<UserResponseDto>> signInUser(
            @AuthenticationPrincipal String userId,
            @AuthenticationPrincipal String password
    ) {
        ResponseDto<UserResponseDto> response = userService.signInUser(userId, password);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("api/v1/singin/users/{id}")
    public ResponseEntity<ResponseDto<UserResponseDto>> getAllUsers(
            @AuthenticationPrincipal String userId, String email, String password,
            @PathVariable Long id
    ) {
        ResponseDto<UserResponseDto> response = userService.getAllUsers(userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseDto<UserResponseDto>> updateUser(
            @PathVariable String userId,
            @RequestBody UserRequestDto dto
    ) {

        ResponseDto<UserResponseDto> response = userService.updateUser(userId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto<Void>> deleteUser(
            @PathVariable Long id,
            @AuthenticationPrincipal String userId
    ) {
        ResponseDto<Void> response = userService.deleteUser(userId);
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
