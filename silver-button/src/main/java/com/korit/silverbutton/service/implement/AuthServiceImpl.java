package com.korit.silverbutton.service.implement;


import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Request.LoginRequestDto;
import com.korit.silverbutton.dto.Request.SignUpRequestDto;
import com.korit.silverbutton.dto.Response.LoginResponseDto;
import com.korit.silverbutton.dto.Response.SignUpResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.provider.JwtProvider;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.commons.validator.routines.EmailValidator;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptpasswordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public ResponseDto<SignUpResponseDto> signUp(@Valid SignUpRequestDto dto) {
        String userId = dto.getUserId(); // 아이디

        String password = dto.getPassword(); // 비밀번호
        String confirmPassword = dto.getConfirmPassword(); // 확인

        String name = dto.getName(); //이름

        String email = dto.getEmail(); //이메일

        String phone = dto.getPhone(); // 전화번호
        String gender = dto.getGender(); //성별

        String nickname = dto.getNickName(); //닉네임

        String rrn = dto.getRrn(); // 주민등록번호

        SignUpResponseDto data = null;

        // 1. 유효성 검사 //
        if (userId == null || userId.isEmpty()) {
            // INVALID_USER_ID
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (password == null || password.isEmpty() || confirmPassword == null || confirmPassword.isEmpty()) {
            // INVALID_PASSWORD
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (!password.equals(confirmPassword)) {
            return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
        }

        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{10,16}$")) {
            // WEAK_PASSWORD
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (name == null || name.isEmpty()) {
            // INVALID_NAME
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (email == null || email.isEmpty() || !EmailValidator.getInstance().isValid(email)) {
            // INVALID_EMAIL
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (phone == null || phone.isEmpty() || !phone.matches("[0-9]{11}$")) { //하이픈없이
            // [0-9]{10,15}$ : 10자에서 15자 사이의 숫자로만 이루어짐

            // INVALID_PHONE
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (gender != null && !gender.matches("M|F")) {
            // INVALID_GENDER
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (nickname == null || nickname.isEmpty() || !nickname.matches("^[가-힣]+$") ) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        if (rrn == null || rrn.isEmpty()) {
            return ResponseDto.setFailed(ResponseMessage.VALIDATION_FAIL);
        }

        // 2. 중복 체크 //
        if (userRepository.existsByUserId(userId)) {
            return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
        }

        if (userRepository.existsByEmail(email)) {
            // EXIST_EMAIL
            return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
        }

        if (userRepository.existsByNickname(nickname)) {
            return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
        }


        try {
            String encodedPassword = bCryptpasswordEncoder.encode(password);

            User user = User.builder()
                    .userId(userId)
                    .password(encodedPassword)
                    .email(email)
                    .name(name)
                    .phone(phone)
                    .gender(gender)
                    .nickname(nickname)
                    .rrn(rrn)
                    .build();

            userRepository.save(user);

            data = new SignUpResponseDto(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<LoginResponseDto> login(LoginRequestDto dto) {
        String userId = dto.getUserId();
        String password = dto.getPassword();

        LoginResponseDto data = null;

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

            String token = jwtProvider.generateJwtToken(userId);
            int exprTime = jwtProvider.getExpiration();

            data = new LoginResponseDto(user, token, exprTime);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

}