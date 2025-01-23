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
        String userId = dto.getUserId();
        String password = dto.getPassword();
        String confirmPassword = dto.getConfirmPassword();
        String name = dto.getName();
        String email = dto.getEmail();
        String phone = dto.getPhone();
        String gender = dto.getGender();
        if ("female".equalsIgnoreCase(gender)) {
            gender = "F";
        } else if ("male".equalsIgnoreCase(gender)) {
            gender = "M";
        } else {
            throw new IllegalArgumentException("Invalid gender value: " + gender);
        }
        String nickname = dto.getNickname();
        Date birthDate= dto.getBirthDate();
        String profileImage= "image";
        String role;
        String licenseNumber= dto.getLicenseNumber();

        int age = calculateAge(birthDate);

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

    private int calculateAge(Date birthDate) {
        Date today = new Date();
        int currentYear = today.getYear() + 1900;
        int birthYear = birthDate.getYear() + 1900;
        int age = currentYear - birthYear;

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

            tokenBlacklistService.addToBlacklist(token);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, "Logout successful");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.LOGOUT_FAILED);
        }

    }
}
