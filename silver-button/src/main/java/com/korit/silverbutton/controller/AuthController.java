package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Request.OverlapIdRequestDto;
import com.korit.silverbutton.dto.User.Request.OverlapNicknameRequestDto;
import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.service.AuthService;
import com.korit.silverbutton.service.TokenBlacklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(SIGN_UP_PATH)
    public ResponseEntity<ResponseDto<SignUpResponseDto>> signUp(@Valid @RequestBody SignUpRequestDto dto) {
        ResponseDto<SignUpResponseDto> response = authService.signUp(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }
    // 회원가입 시, 65세 이상일 경우 자동적으로 '노인'role 적용, but 요양사일 경우 role을 강제적으로 '요양사'role
    @PostMapping(LOGIN_PATH)
    public ResponseEntity<ResponseDto<SignInResponseDto>> login(@Valid @RequestBody SignInRequestDto dto) {
        ResponseDto<SignInResponseDto> response = authService.login(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    // 간편 로그인 (dependentLogin)
    @PostMapping(DEPENDENT_LOGIN_PATH)
    public ResponseEntity<ResponseDto<SignInResponseDto>> dependentLogin(@Valid @RequestBody SignInRequestDto dto) {
        ResponseDto<SignInResponseDto> response = authService.dependentLogin(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(LOGOUT_PATH)
    public ResponseEntity<ResponseDto<String>> logout(@RequestHeader("Authorization") String tokenHeader) {
        try {
            // Bearer 토큰에서 'Bearer ' 부분을 제거하고 실제 토큰을 추출
            String token = jwtProvider.removeBearer(tokenHeader);

            // 블랙리스트에 토큰을 추가하여 무효화
            authService.logout(token);

            // 응답
            ResponseDto<String> response = ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            ResponseDto<String> response = ResponseDto.setFailed("로그아웃 오류 발생");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/check-duplicate-userid")
    public ResponseEntity<ResponseDto<Boolean>> duplicateCheckUserId(
            @RequestBody OverlapIdRequestDto dto){
        boolean isDuplicated= authService.overlapUserId(dto.getUserId());
        ResponseDto<Boolean> response= isDuplicated? ResponseDto.setFailed("userId가 중복됩니다."):
                ResponseDto.setSuccess("userId가 사용가능합니다.", isDuplicated);

        HttpStatus status = isDuplicated ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/check-duplicate-nickname")
    public ResponseEntity<ResponseDto<Boolean>> duplicateCheckNickName(
            @RequestBody OverlapNicknameRequestDto dto){
        boolean isDuplicated= authService.overlapNickname(dto.getNickname());
        ResponseDto<Boolean> response= isDuplicated? ResponseDto.setFailed("닉네임이 중복됩니다."):
        ResponseDto.setSuccess("닉네임이 사용가능합니다.", isDuplicated);

        HttpStatus status = isDuplicated ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

}
