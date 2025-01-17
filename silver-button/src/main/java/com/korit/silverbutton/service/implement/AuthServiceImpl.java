package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignUp.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;
import com.korit.silverbutton.dto.SignUp.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SnsLoginResponseDto;
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
import java.util.Optional;

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
    public ResponseDto<SnsLoginResponseDto> findUserIdByEmail(String email) {
        try {
            // 이메일로 사용자를 찾기 (Optional<User> 반환)
            Optional<User> optionalUser = userRepository.findByEmail(email);

            // 사용자가 없으면 예외를 던짐
            User user = optionalUser.orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

            // SnsLoginResponseDto 생성 (아이디만 반환)
            SnsLoginResponseDto responseDto = SnsLoginResponseDto.fromUser(user.getUserId(), user.getEmail(), false, "아이디 찾기 성공");

            // 성공 응답 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);  // 기타 오류 발생 시 데이터베이스 오류 메시지 반환
        }
    }

    @Override
    public ResponseDto<SnsLoginResponseDto> verifyUserId(String email, String token) {
        try {
            // 이메일 인증 토큰을 검증하여 유효한 아이디를 가져옴
            String userId = mailService.verifyEmail(token);

            // 인증이 성공했을 때, SnsLoginResponseDto 생성
            SnsLoginResponseDto responseDto = new SnsLoginResponseDto(userId, email, false, "아이디 인증 성공");

            // 성공적인 응답 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
        } catch (IllegalArgumentException e) {
            // 예외가 발생한 경우 (예: 잘못된 토큰)
            return ResponseDto.setFailed("유효하지 않은 인증 토큰입니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("아이디 인증에 실패했습니다.");
        }
    }
    @Override
    public ResponseDto<SnsLoginResponseDto> sendPasswordResetLink(String email) {
        try {
            // 이메일로 사용자 찾기 (Optional<User> 반환)
            Optional<User> optionalUser = userRepository.findByEmail(email);

            // 사용자가 없으면 예외를 던짐
            User user = optionalUser.orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

            // 비밀번호 재설정 링크 발송
            String token = mailService.sendSimpleMessage(email, user.getUserId());

            // 비밀번호 재설정 성공 응답 반환
            SnsLoginResponseDto responseDto = new SnsLoginResponseDto(user.getUserId(), user.getEmail(), true, "비밀번호 재설정 링크가 발송되었습니다.");

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);  // 기타 오류 발생 시 데이터베이스 오류 메시지 반환
        }
    }

    @Override
    public ResponseDto<SnsLoginResponseDto> resetPassword(String token, String newPassword) {
        try {
            // 이메일 인증 토큰을 검증하여 유효한 사용자 ID 가져옴
            String userId = mailService.verifyEmail(token);

            // 해당 userId로 사용자 찾기
            Optional<User> optionalUser = userRepository.findByUserId(userId);

            // 사용자가 없으면 예외를 던짐
            User user = optionalUser.orElseThrow(() -> new IllegalArgumentException(ResponseMessage.NOT_EXIST_USER));

            // 새로운 비밀번호로 업데이트
            String encodedPassword = bCryptpasswordEncoder.encode(newPassword);
            user.setPassword(encodedPassword);
            userRepository.save(user);

            // 비밀번호 재설정 성공 응답 반환
            SnsLoginResponseDto responseDto = new SnsLoginResponseDto(user.getUserId(), user.getEmail(), false, "비밀번호가 성공적으로 재설정되었습니다.");

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, responseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("비밀번호 재설정에 실패했습니다.");
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
