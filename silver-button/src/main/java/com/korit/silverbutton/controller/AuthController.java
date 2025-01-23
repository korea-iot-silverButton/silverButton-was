package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SnsLoginResponseDto;
import com.korit.silverbutton.dto.User.Request.OverlapIdRequestDto;
import com.korit.silverbutton.dto.User.Request.OverlapNicknameRequestDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.service.AuthService;
import com.korit.silverbutton.service.TokenBlacklistService;
import com.korit.silverbutton.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping(ApiMappingPattern.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProvider jwtProvider;
    private final TokenBlacklistService tokenBlacklistService;

    private static final String SIGN_UP_PATH = "/signup";
    private static final String LOGIN_PATH = "/login";
    private static final String DEPENDENT_LOGIN_PATH = "/dependent-login";
    private static final String LOGOUT_PATH = "/logout";
    private static final String MAIL = "/mail";


    private final UserService userService;

    @PostMapping(SIGN_UP_PATH)
    public ResponseEntity<ResponseDto<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        ResponseDto<SignUpResponseDto> response = authService.signUp(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(LOGIN_PATH)
    public ResponseEntity<ResponseDto<SignInResponseDto>> login(@Valid @RequestBody SignInRequestDto dto) {
        ResponseDto<SignInResponseDto> response = authService.login(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(DEPENDENT_LOGIN_PATH)
    public ResponseEntity<ResponseDto<SignInResponseDto>> dependentLogin(@Valid @RequestBody SignInRequestDto dto) {
        ResponseDto<SignInResponseDto> response = authService.dependentLogin(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(LOGOUT_PATH)
    public ResponseEntity<ResponseDto<String>> logout(@RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = jwtProvider.removeBearer(tokenHeader);
            authService.logout(token);

            ResponseDto<String> response = ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ResponseDto<String> response = ResponseDto.setFailed("로그아웃 오류 발생");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/check-duplicate-userId")
    public ResponseEntity<ResponseDto<Boolean>> duplicateCheckUserId(
            @RequestBody OverlapIdRequestDto dto){
        System.out.println("숨1");
        boolean isDuplicated= authService.overlapUserId(dto.getUserId());
        System.out.println("숨2");
        ResponseDto<Boolean> response= isDuplicated? ResponseDto.setFailed("userId가 중복됩니다."):
                ResponseDto.setSuccess("userId가 사용가능합니다.", isDuplicated);

        System.out.println(isDuplicated);
        HttpStatus status = isDuplicated ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<ResponseDto<Boolean>> duplicateCheckNickName(
            @RequestBody OverlapNicknameRequestDto dto){
        boolean isDuplicated= authService.overlapNickname(dto.getNickname());
        ResponseDto<Boolean> response= isDuplicated? ResponseDto.setFailed("닉네임이 중복됩니다."):
        ResponseDto.setSuccess("닉네임이 사용가능합니다.", isDuplicated);
        HttpStatus status = isDuplicated ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
        return ResponseEntity.status(status).body(response);
    }

    @PutMapping("/upload-profile-img")
    public ResponseEntity<ResponseDto<String>> uploadProfileImg(
            @AuthenticationPrincipal PrincipalUser principalUser,
            @RequestParam("file") MultipartFile file) {
        ResponseDto<String> response = userService.uploadFile(principalUser.getUserId(), file);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/profile-img")
    public ResponseEntity<ResponseDto<String>> getProfileImg(@AuthenticationPrincipal PrincipalUser principalUser) {
        ResponseDto<String> response = userService.getProfileImg(principalUser.getUserId());
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }


}
