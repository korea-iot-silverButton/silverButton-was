package com.korit.silverbutton.service.implement;


import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.AuthService;
import com.korit.silverbutton.service.TokenBlacklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptpasswordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    public ResponseDto<SignUpResponseDto> signUp(@Valid SignUpRequestDto dto) {
        String userId = dto.getUserId(); // 아이디

        String password = dto.getPassword(); // 비밀번호
        String confirmPassword = dto.getConfirmPassword(); // 확인

        String name = dto.getName(); //이름

        String email = dto.getEmail(); //이메일

        String phone = dto.getPhone(); // 전화번호
        String gender = dto.getGender(); //성별

        String nickname = dto.getNickname(); // 닉네임

        Date birthDate= dto.getBirthDate(); // 나이 계산

        String profileImage= dto.getProfileImage();

        String role;

        String licenseNumber= dto.getLicenseNumber();

        // 현재 날짜 기준으로 나이를 계산
        int age = calculateAge(birthDate);

        // role 설정 로직
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            role = "요양사";
        } else if (age >= 65) {
            role = "노인";
        } else {
            role = "보호자";
        }

        String specialization= dto.getSpecialization();

        Long protectorId= dto.getProtectorId();

        SignUpResponseDto data = null;



        try {
            String encodedPassword = bCryptpasswordEncoder.encode(password);

            User user = User.builder()
                    .userId(userId)
                    .password(encodedPassword)
                    .name(name)
                    .phone(phone)
                    .email(email)
                    .nickname(nickname)
                    .birthDate(birthDate)
                    .gender(gender)
                    .profileImage(profileImage)
                    .role(role)
                    .licenseNumber(licenseNumber)
                    .specialization(specialization)
                    .protectorId(protectorId)
                    .build();

            userRepository.save(user);

            data = new SignUpResponseDto(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 나이 계산 메서드
    private int calculateAge(Date birthDate) {
        Date today = new Date();
        int currentYear = today.getYear() + 1900;
        int birthYear = birthDate.getYear() + 1900;

        int age = currentYear - birthYear;

        // 생일이 아직 지나지 않았으면 나이에서 1을 뺌
        if (today.getMonth() < birthDate.getMonth() ||
                (today.getMonth() == birthDate.getMonth() && today.getDate() < birthDate.getDate())) {
            age--;
        }

        return age;
    }

    @Override
    public ResponseDto<SignInResponseDto> login(SignInRequestDto dto) {
        String userId = dto.getUserId();
        String password = dto.getPassword();

        SignInResponseDto data = null;

        // 1. 유효성 검사 //
        if (userId == null || userId.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (password == null || password.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        try {
            User user = userRepository.findByUserId(userId)
                    .orElse(null);

            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            if (!bCryptpasswordEncoder.matches(password, user.getPassword())) {
                return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
            }

            // JWT 생성시 isDependentLogin 값은 false로 설정
            String token = jwtProvider.generateJwtToken(userId, false);
            int exprTime = jwtProvider.getExpiration();

            data = new SignInResponseDto(user, token, exprTime);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<SignInResponseDto> dependentLogin(SignInRequestDto dto) {
        // 간편 로그인 로직
        String name = dto.getName();
        String phone = dto.getPhone();
        SignInResponseDto data = null;

        if (name == null || name.isEmpty() || phone == null || phone.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        try {
            User user = userRepository.findByNameAndPhone(name, phone).orElse(null);

            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            if (!"노인".equals(user.getRole())) {
                return ResponseDto.setFailed(ResponseMessage.UNAUTHORIZED);
            }

            // JWT 생성시 isDependentLogin 값은 true로 설정
            String token = jwtProvider.generateJwtToken(user.getUserId(), true);
            int exprTime = jwtProvider.getExpiration();
            data = new SignInResponseDto(user, token, exprTime);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<String> logout(String token) {
        try {
            // 토큰을 블랙리스트에 추가
            tokenBlacklistService.addToBlacklist(token);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, "Logout successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.LOGOUT_FAILED);
        }
    }
}