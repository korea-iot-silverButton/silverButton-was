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

    @PostMapping("/check-duplicate-userId") // http://localhost:4040/api/v1/auth/check-duplicate-userid
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

    @PostMapping("/check-duplicate-nickname") // http://localhost:4040/api/v1/auth/check-duplicate-nickname
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

    // 이메일 인증을 통한 아이디 찾기
    @PostMapping("/findId")
    public ResponseDto<String> findUserIdByEmail(@RequestParam String email) {
        return authService.findUserIdByEmail(email);
    }

    // 이메일 인증 후 아이디 찾기
    @PostMapping("/verifyId")
    public ResponseDto<String> verifyUserId(@RequestParam String email, @RequestParam String token) {
        return authService.findUserIdByEmail(email); // 이미 findUserIdByEmail 메서드로 처리 가능
    }

    // 비밀번호 재설정 링크 발송
    @PostMapping("/findPassword")
    public ResponseDto<String> sendPasswordResetLink(@RequestParam String email) {
        return authService.sendPasswordResetLink(email);
    }

    // 비밀번호 재설정 및 변경
    @PostMapping("/verifyPassword")
    public ResponseDto<String> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        return authService.resetPassword(token, newPassword);
    }

//    // 카카오 로그인 요청
//    @PostMapping("/kakao")
//    public ResponseEntity<String> loginWithKakao(@RequestParam String code) {
//        try {
//            User user = authService.loginWithKakao(code);
//            return ResponseEntity.ok("Kakao Login Success: " + user.getUsername());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Kakao login failed: " + e.getMessage());
//        }
//    }
//
//    // 네이버 로그인 요청
//    @PostMapping("/naver")
//    public ResponseEntity<String> loginWithNaver(@RequestParam String code) {
//        try {
//            User user = authService.loginWithNaver(code);
//            return ResponseEntity.ok("Naver Login Success: " + user.getUsername());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Naver login failed: " + e.getMessage());
//        }
//    }



}
