package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.dto.User.Request.*;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.service.AuthService;
import com.korit.silverbutton.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ApiMappingPattern.MANAGE)
@RequiredArgsConstructor
public class UserController {

    private final @Lazy UserService userService;
    private final @Lazy AuthService authService;

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

    // 비밀번호 업데이트 API
    @PutMapping("/update-password")
    public ResponseEntity<ResponseDto<UserProfileDto>> updatePassword(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestBody @Valid UpdatePasswordRequestDto dto) {

        // 비밀번호 업데이트 처리
        ResponseDto<UserProfileDto> response = userService.updatePassword(
                principalUser.getUserId(), dto.getCurrentPassword(), dto.getNewPassword());

        // 상태 코드 설정 (성공이면 200, 실패하면 400)
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    // 비밀번호 검증 API
    @PostMapping("/verify-password")
    public ResponseEntity<ResponseDto<Boolean>> verifyPassword(
            @AuthenticationPrincipal PrincipalUser principalUser,  // 현재 로그인된 사용자 정보
            @RequestBody PasswordVerifyRequestDto dto  // 비밀번호 검증 요청 DTO
    ) {
        boolean isPasswordValid = userService.verifyPassword(principalUser.getUserId(), dto.getCurrentPassword());
        ResponseDto<Boolean> response = isPasswordValid ?
                ResponseDto.setSuccess("비밀번호가 일치합니다.", isPasswordValid) :
                ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");

        HttpStatus status = isPasswordValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<ResponseDto<Void>> deleteUser(
            @AuthenticationPrincipal PrincipalUser principalUser
    ) {
        ResponseDto<Void> response = userService.deleteUser(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }//complete

    @PutMapping("/upload-profile-img")
    public ResponseEntity<ResponseDto<String>> uploadProfileImg(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestParam("file") MultipartFile file) {
        // 새 이미지를 업로드
        ResponseDto<String> response = userService.uploadFile(principalUser.getUserId(), file);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }// Test해봐야 함

    @GetMapping("/profile-img")
    public ResponseEntity<ResponseDto<String>> getProfileImg(@AuthenticationPrincipal PrincipalUser principalUser) {
        // 현재 로그인된 사용자의 프로필 이미지 경로를 반환
        ResponseDto<String> response = userService.getProfileImg(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }// Test 해봐야 함
}
