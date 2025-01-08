package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.ProfileImgService;
import com.korit.silverbutton.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileImgService profileImgService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    // user 조회
    public ResponseDto<List<UserResponseDto>> getAllUsers(String userId) {
        try{
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            List<User> allUsers = userRepository.findAll();
            List<UserResponseDto> data = allUsers.stream()
                    .map(UserResponseDto::new)
                    .toList();
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }//http://localhost:4040/api/v1/manage/allusers 오퍼레이션 확인

    @Override
    public ResponseDto<UserProfileDto> getUser(String userId) {
        try {
            Optional<User> userOptional = userRepository.findByUserId(userId);

            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            UserProfileDto userProfileDto = new UserProfileDto(userOptional.get());
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, userProfileDto);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }



    //http://localhost:4040/api/v1/manage/profile 오퍼 확인

    // 사용자 정보 수정 및 업데이트
    @Override
    public ResponseDto<UserProfileDto> updateUser(String userId, UserProfileDto dto) {
        try {
            // 사용자 ID로 해당 사용자 검색
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 사용자가 존재하면 해당 사용자 객체 가져오기
            User user = userOptional.get();

            // 이메일 중복 체크 (이메일이 변경되었을 경우만 체크)
            if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
                return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
            }

            // 전화번호 중복 체크 (전화번호가 변경되었을 경우만 체크)
            if (!user.getPhone().equals(dto.getPhone()) && userRepository.existsByPhone(dto.getPhone())) {
                return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
            }

            // 닉네임 중복 체크 (닉네임이 변경되었을 경우만 체크)
            if (!user.getNickname().equals(dto.getNickname()) && userRepository.existsByNickname(dto.getNickname())) {
                return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
            }

            // 비밀번호는 변경하지 않음
            // 비밀번호 변경 없이 다른 값들만 업데이트
            user = user.toBuilder()
                    .email(dto.getEmail())  // 이메일 변경
                    .phone(dto.getPhone())  // 전화번호 변경
                    .nickname(dto.getNickname())  // 닉네임 변경
                    .build(); // 이메일, phone, 닉네임만 변경가능

            // 사용자 정보 저장
            userRepository.save(user);

            // 업데이트된 사용자 정보 반환
            UserProfileDto data = new UserProfileDto(user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    } // http://localhost:4040/api/v1/manage/allusers 작동 함


    @Override
    public ResponseDto<Void> deleteUser(String userId) {
        try {
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();
            userRepository.delete(user);

            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }
    //http://localhost:4040/api/v1/manage/delete-account 작동 확인

    // 프로필 이미지 업로드
    public ResponseDto<String> uploadFile(String userId, MultipartFile file) {
        try {
            // 사용자 확인
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 프로필 이미지 업로드
            String filePath = profileImgService.uploadFile(file);

            if (filePath == null) {
                return ResponseDto.setFailed("PROFILE_IMG_UPLOAD_FAIL");
            }

            // 이미지 경로 DB에 저장
            User user = userOptional.get();
            user.setProfileImg(filePath);
            userRepository.save(user);

            return ResponseDto.setSuccess("PROFILE_IMG_UPLOAD_SUCCESS", filePath);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_UPDATE_FAIL");
        }
    }

    // 프로필 이미지 삭제
    public ResponseDto<Void> deleteFile(String userId) {
        try {
            // 사용자 확인
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();
            String profileImg = user.getProfileImg();

            // 기존 프로필 이미지 삭제
            if (profileImg != null) {
                boolean deleted = profileImgService.deleteFile(profileImg);
                if (deleted) {
                    user.setProfileImg(null);
                    userRepository.save(user);
                    return ResponseDto.setSuccess("PROFILE_IMG_DELETE_SUCCESS", null);
                } else {
                    return ResponseDto.setFailed("PROFILE_IMG_DELETE_FAIL");
                }
            }
            return ResponseDto.setFailed("PROFILE_IMG_DELETE_FAIL");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_DELETE_FAIL");
        }
    }

    // 프로필 이미지 조회
    public ResponseDto<String> getProfileImg(String userId) {
        try {
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            String profileImg = userOptional.get().getProfileImg();
            if (profileImg != null) {
                return ResponseDto.setSuccess("PROFILE_IMG_NOT_FOUND", profileImg);
            }
            return ResponseDto.setFailed("PROFILE_IMG_NOT_FOUND");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_NOT_FOUND");
        }
    }
}

// 프로필 이미지만 어떻게 좀 해야되는데
