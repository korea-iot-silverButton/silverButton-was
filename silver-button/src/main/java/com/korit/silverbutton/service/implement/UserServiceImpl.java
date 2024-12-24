package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.dto.User.Request.UserRequestDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.ProfileImgService;
import com.korit.silverbutton.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProfileImgService profileImgService;

    @Override
    // 로그인
    public ResponseDto<UserResponseDto> signInUser(String userId, String password) {
        UserResponseDto data = null;
        try {
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            User user = userOptional.get(); // 사용자 정보 가져오기

            // 비밀번호 일치 체크
            if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
            }

            // 로그인 성공
            UserResponseDto userResponseDto = new UserResponseDto(user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, userResponseDto);

        } catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    // user 조회
    public ResponseDto<UserResponseDto> getAllUsers(String userId) {
        try{
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();
            UserResponseDto data = new UserResponseDto(user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

    }

    // 사용자 정보 수정 및 업데이트
    @Override
    public ResponseDto<UserResponseDto> updateUser(String userId, UserRequestDto dto) {
        try {
            // 사용자 ID로 해당 사용자 검색
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 사용자가 존재하면 해당 사용자 객체 가져오기
            User user = userOptional.get();

            // 이메일 중복 체크
            if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
            }

            // 사용자 정보 업데이트
            user = user.toBuilder()
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .nickname(dto.getNickname())
                    .password(dto.getPassword())
                    .build();

            // 사용자 정보 저장
            userRepository.save(user);

            // 업데이트된 사용자 정보 반환
            UserResponseDto data = new UserResponseDto(user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }


    @Override
    public ResponseDto<Void> deleteUser(String userId) {
        try {
            // 사용자 ID로 검색
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 사용자 존재하면 삭제
            User user = userOptional.get();
            userRepository.delete(user);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
}
