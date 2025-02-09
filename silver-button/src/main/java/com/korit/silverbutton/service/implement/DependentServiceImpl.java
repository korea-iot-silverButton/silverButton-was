package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.Dependent.ResponseDto.DependentResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.SignIn.Request.SignInRequestDto;
import com.korit.silverbutton.dto.SignIn.Response.SignInResponseDto;

import com.korit.silverbutton.dto.UpdateRequestDto;
import com.korit.silverbutton.entity.User;
import com.korit.silverbutton.principal.PrincipalUser;
import com.korit.silverbutton.repository.UserRepository;

import com.korit.silverbutton.service.DependentService;
import com.korit.silverbutton.service.ProfileImgService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DependentServiceImpl implements DependentService {

    private final UserRepository userRepository;
    private final ProfileImgService profileImgService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseDto<SignInResponseDto> depenLogin(SignInRequestDto dto) {
        try {
            // 사용자 조회
            User user = userRepository.findByRoleAndNameAndPhone(dto.getRole(),dto.getName(), dto.getPhone());
            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }

            // 비밀번호 검증
            if (!bCryptPasswordEncoder.matches(dto.getPassword(), user.getPassword())) {
                return ResponseDto.setFailed(ResponseMessage.NOT_MATCH_PASSWORD);
            }

            // DTO 변환
            DependentResponseDto dependentResponseDto = new DependentResponseDto(user);
            SignInResponseDto signInResponseDto = new SignInResponseDto(user);

            // 성공 응답 반환
            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, signInResponseDto);

        } catch (Exception e) {
            // 예외 처리
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
    }

    // 노인 사용자 로그인 요청 처리
    @Override
    public ResponseDto<DependentResponseDto> signInDepen(String role,String name, String phone) {
        DependentResponseDto dependentResponseDto = null; // DTO 객체 초기화
        try {
            // 노인 사용자가 존재하는지는 이름이랑 전화번호로 조회
            User user = userRepository.findByRoleAndNameAndPhone(role,name, phone);

            if (user == null) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            // 사용자가 노인이 맞는지 확인하는 if문
            if (!"노인".equals(user.getRole())) {
                return ResponseDto.setFailed(ResponseMessage.NO_PERMISSION);
            }
            // 사용자 정보를 DependentResponseDto 객체로 변환
            dependentResponseDto = new DependentResponseDto(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, dependentResponseDto);
    }

    @Override
    // 특정 노인 사용자 조회인데 로그인에서는 필요없을거 같아 ㅠ
    // 매칭 , 약품 데이터 , 게시판이나 이런 기능에서는 필요하겠지만 나는 필요없을듯
    // 조장님 확인해주세용 필요하면 만들게요 ㅇㅅㅇ
    public ResponseDto<DependentResponseDto> getAllDepen(Long id, String userId) {
        try{

        } catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return null;
    }


    @Override
    public ResponseDto<DependentResponseDto> updateDepen(UpdateRequestDto dto, PrincipalUser principalUser) {
        DependentResponseDto data = null;
        String email = dto.getEmail();
        String name = dto.getName();
        String phone = dto.getPhone();
        String nickname = dto.getNickname();

        String role = principalUser.getRole();
        try{
            // principalUser의 역할 포함해서 노인을 조회(조회시 name과 phone으로 노인인지 확인)
            User user = userRepository.findByRoleAndNameAndPhone(principalUser.getRole(), name, phone);

            if (user == null) {
                //  사용자가 존재하지 않으면 실패 응답 반환
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            // 권한 검증: principalUser와 user가 동일한지 확인
            if (!user.getName().equals(principalUser.getName()) || !user.getPhone().equals(principalUser.getPhone())) {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);
            }
            // 사용자 정보 업데이트
            user = user.toBuilder()
                    .email(email != null ? email : user.getEmail()) // email이 null이 아니면 업데이트
                    .name(name != null ? name : user.getName()) // 이름
                    .phone(phone != null ? phone : user.getPhone()) // 폰
                    .nickname(nickname != null ? nickname : user.getNickname()) // 닉네임
                    .build();

            userRepository.save(user);

            data = new DependentResponseDto(user);


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        // 성공
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);

    }

    @Override
    public ResponseDto<Void> deleteDepen(String role, String name, String phone) {

        ResponseDto<Void> data = null;
        try {
            // 노인 조회
            User user = userRepository.findByRoleAndNameAndPhone(role, name, phone);

            if (user == null) ResponseDto.setFailed(ResponseMessage.NOT_EXIST_USER);

            userRepository.delete(user);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);

        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}
