package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.MANAGE)
@RequiredArgsConstructor
public class UserController {

    private final @Lazy UserService userService;

    @GetMapping("/allusers")
    public ResponseEntity<ResponseDto<List<UserResponseDto>>> getAllUsers(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<List<UserResponseDto>> response = userService.getAllUsers(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }//complete

    @GetMapping("/profile")
    public ResponseEntity<ResponseDto<UserProfileDto>> getuser(
            @AuthenticationPrincipal PrincipalUser principalUser
    ){
        ResponseDto<UserProfileDto> response= userService.getUser(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }//complete

    @PutMapping("/update")
    public ResponseEntity<ResponseDto<UserProfileDto>> updateUser(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @ModelAttribute UserProfileDto dto // 요청 데이터를 받기 위해 @RequestBody 사용
            // @ModelAttribute >> formdata를 전송하기 위해 해당 어노테이션 필요
    ) {
        ResponseDto<UserProfileDto> response = userService.updateUser(principalUser.getUserId(), dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    } // complete

    @DeleteMapping("/delete-account")
    public ResponseEntity<ResponseDto<Void>> deleteUser(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<Void> response = userService.deleteUser(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }//complete

}
