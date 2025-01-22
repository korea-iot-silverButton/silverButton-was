package com.korit.silverbutton.service.implement;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.User.Response.UserProfileDto;
import com.korit.silverbutton.dto.User.Response.UserResponseDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.repository.UserRepository;
import com.korit.silverbutton.service.MailService;
import com.korit.silverbutton.service.ProfileImgService;
import com.korit.silverbutton.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProfileImgService profileImgService; // 프로필 이미지 추가
    private final BCryptPasswordEncoder bCryptPasswordEncoder; // 비번 검증
    private final MailService mailService; // 이메일 인증을 위한 서비스 주입
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Autowired
    private Storage storage;

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
            System.out.println("사용자"+userId);
            // 사용자 ID로 해당 사용자 검색
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            System.out.println("afasdfawefe");
            // 사용자가 존재하면 해당 사용자 객체 가져오기
            User user = userOptional.get();

            // 이메일 중복 체크 (이메일이 변경되었을 경우만 체크)
//            if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
//                return ResponseDto.setFailed(ResponseMessage.EXIST_USER);
//            }

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
    public ResponseDto<UserProfileDto> updatePassword(String userId, String currentPassword, String newPassword) {
        try {
            // 사용자 ID로 해당 사용자 검색
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 사용자가 존재하면 해당 사용자 객체 가져오기
            User user = userOptional.get();

            // 현재 비밀번호 검증 (입력한 비밀번호가 DB에 저장된 비밀번호와 일치하는지 확인)
            if (!bCryptPasswordEncoder.matches(currentPassword, user.getPassword())) {
                return ResponseDto.setFailed("CURRENT_PASSWORD_INCORRECT");
            }

            // 새 비밀번호가 너무 간단하지 않은지 추가적으로 체크할 수 있음 (예: 길이, 특수문자 포함 등)
            if (newPassword.length() < 8) {
                return ResponseDto.setFailed("NEW_PASSWORD_TOO_SHORT");
            }

            // 새 비밀번호 암호화
            String encodedNewPassword = bCryptPasswordEncoder.encode(newPassword);

            // 비밀번호만 변경
            user.setPassword(encodedNewPassword);

            // 사용자 정보 저장
            userRepository.save(user);

            // 변경된 사용자 정보를 반환
            UserProfileDto data = new UserProfileDto(user);
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    @Override
    public boolean verifyPassword(String userId, String currentPassword) {
        try {
            // 사용자 조회
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return false;  // 사용자가 존재하지 않으면 false 반환
            }

            User user = userOptional.get();

            // 현재 비밀번호와 저장된 암호화된 비밀번호 비교
            return bCryptPasswordEncoder.matches(currentPassword, user.getPassword());

        } catch (Exception e) {
            e.printStackTrace();
            return false;  // 예외 발생 시 false 반환
        }
    }//http://localhost:4040/api/v1/manage/verify-password

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
    @Override
    public ResponseDto<String> uploadFile(String userId, MultipartFile file) {
        try {
            // 사용자 확인
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            User user = userOptional.get();
            String currentProfileImg = user.getProfileImage();  // 기존 프로필 이미지 가져오기

            // 기존 파일이 있다면 삭제
            if (currentProfileImg != null && !currentProfileImg.isEmpty()) {
                // URL에서 파일 이름(UUID)만 추출
                String fileName = currentProfileImg.substring(currentProfileImg.lastIndexOf("/") + 1);

                // 기존 파일 삭제
                Blob blob = storage.get(BlobId.of(bucketName, fileName));

                // blob이 null이 아니고 파일이 존재하는 경우에만 삭제
                if (blob != null && blob.exists()) {
                    blob.delete();  // 파일 삭제
                } else {
                    System.out.println("null");
                }
            }

            // 새로 업로드할 파일 이름(UUID) 생성
            String uuid = UUID.randomUUID().toString();
            String ext = file.getContentType();

            // Google Cloud Storage에 새 파일 업로드
            BlobInfo blobInfo = storage.create(
                    BlobInfo.newBuilder(bucketName, uuid)
                            .setContentType(ext)
                            .build(),
                    file.getInputStream()
            );

            String profileImgUrl = String.format("https://storage.googleapis.com/%s/%s", bucketName, uuid);

            // 프로필 이미지 경로 DB에 저장
            user.setProfileImage(profileImgUrl);  // DB의 프로필 이미지 경로 업데이트
            userRepository.save(user);  // DB에 저장

            return ResponseDto.setSuccess("PROFILE_IMG_UPLOAD_SUCCESS", profileImgUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_UPDATE_FAIL");
        }
    }// patch 형식으로 http://localhost:4040/api/v1/manage/upload-profile-img

    // 프로필 이미지 삭제
    @Override
    public ResponseDto<Void> deleteFile(String filePath) {
        try {
            // 사용자 확인
            Optional<User> userOptional = userRepository.findByUserId(filePath);
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
    @Override
    public ResponseDto<String> getProfileImg(String userId) {
        try {
            Optional<User> userOptional = userRepository.findByUserId(userId);
            if (userOptional.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            String profileImg = userOptional.get().getProfileImage();
            if (profileImg != null) {
                System.out.println(profileImg);
                return ResponseDto.setSuccess("PROFILE_IMG_FOUND", profileImg);
            }
            return ResponseDto.setFailed("PROFILE_IMG_NOT_FOUND");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed("PROFILE_IMG_NOT_FOUND");
        }
    }//http://localhost:4040/api/v1/manage/profile-img
}



