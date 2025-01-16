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
import com.korit.silverbutton.service.MailService;
import com.korit.silverbutton.service.TokenBlacklistService;
import jakarta.mail.MessagingException;
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
    private final MailService mailService;

    @Override
    public ResponseDto<SignUpResponseDto> signUp(SignUpRequestDto dto) {
        String userId = dto.getUserId(); // 아이디
        String password = dto.getPassword(); // 비밀번호
        String confirmPassword = dto.getConfirmPassword(); // 확인
        String name = dto.getName(); //이름
        String email = dto.getEmail(); //이메일
        String phone = dto.getPhone(); // 전화번호
        String gender = dto.getGender(); //성별
        if ("female".equalsIgnoreCase(gender)) {
            gender = "F";
        } else if ("male".equalsIgnoreCase(gender)) {
            gender = "M";
        } else {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }
        String nickname = dto.getNickname(); // 닉네임
        Date birthDate= dto.getBirthDate(); // 나이 계산
        String profileImage= "image"; // 프로필 이미지 기본값
        String role;
        String licenseNumber= dto.getLicenseNumber();

        // 나이 계산 메서드
        int age = calculateAge(birthDate);

        // role 설정 로직
        if (licenseNumber != null && !licenseNumber.isEmpty()) {
            role = "요양사";
        } else if (age >= 65) {
            role = "노인";
        } else {
            role = "보호자";
        }

        String specialization = dto.getSpecialization();
        Long protectorId = dto.getProtectorId();

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

        // 1. 유효성 검사
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
            String role = user.getRole();
            String token = jwtProvider.generateJwtToken(userId, false, role);
            int exprTime = jwtProvider.getExpiration();

            data = new SignInResponseDto(user, token, exprTime);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public boolean overlapUserId(String Id) {
        if (Id == null || Id.isEmpty()) {
            return false;
        }
        return userRepository.existsByUserId(Id);
    }

    @Override
    public boolean overlapNickname(String Nickname) {
        if (Nickname == null || Nickname.isEmpty()) {
            return false;
        }
        return userRepository.existsByNickname(Nickname);
    }

    // 이메일 관련 아이디 비번 찾기 로직
    @Override
    public ResponseDto<String> findUserIdByEmail(String email) {
        try {
            // 이메일로 사용자를 찾기
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));  // 사용자가 없으면 예외 발생

            // 사용자 아이디 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, user.getUserId());

        } catch (IllegalArgumentException e) {
            // 예외가 발생한 경우, ResponseMessage를 이용한 실패 메시지 반환
            return ResponseDto.setFailed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);  // 기타 오류 발생 시 데이터베이스 오류 메시지 반환
        }
    }

    // 찾은 이메일로 아이디
    @Override
    public ResponseDto<String> verifyUserId(String email, String token) {
        try {
            // 인증 토큰을 통해 이메일 인증 처리
            String userId = mailService.verifyEmail(token);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, "아이디는 " + userId + "입니다.");
        } catch (Exception e) {
            return ResponseDto.setFailed("아이디 인증에 실패했습니다.");
        }
    }

    // 이메일로 비번
    @Override
    public ResponseDto<String> sendPasswordResetLink(String email) {
        try {
            // 이메일로 사용자 찾기
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));  // 사용자가 없으면 예외 발생

            // 인증 메일 발송
            String token = mailService.sendSimpleMessage(email, user.getUserId());  // 인증 메일 발송

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, "비밀번호 재설정 링크가 발송되었습니다.");
        } catch (IllegalArgumentException e) {
            // 사용자가 존재하지 않을 경우
            return ResponseDto.setFailed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);  // 기타 오류 발생 시 데이터베이스 오류 메시지 반환
        }
    }

    // 인증된 이메일로 비번 변경
    @Override
    public ResponseDto<String> resetPassword(String token, String newPassword) {
        try {
            // 토큰 검증
            String userId = mailService.verifyEmail(token);  // 토큰 검증

            // 사용자 찾기
            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));  // 사용자가 없으면 예외 발생

            // 새로운 비밀번호로 업데이트
            String encodedPassword = bCryptpasswordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, "비밀번호가 성공적으로 재설정되었습니다.");
        } catch (IllegalArgumentException e) {
            // 사용자가 존재하지 않을 경우
            return ResponseDto.setFailed(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("비밀번호 재설정에 실패했습니다.");  // 비밀번호 재설정 실패 메시지 반환
        }
    }

    @Override
    public ResponseDto<SignInResponseDto> dependentLogin(SignInRequestDto dto) {
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

            String role = user.getRole();
            String token = jwtProvider.generateJwtToken(user.getUserId(), true, role);
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
