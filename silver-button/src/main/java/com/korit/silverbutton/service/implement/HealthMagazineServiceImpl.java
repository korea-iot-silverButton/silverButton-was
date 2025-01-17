package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.HealthMagazine.request.HealthMagazineRequestDto;
import com.korit.silverbutton.dto.HealthMagazine.response.HealthMagazineResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.entity.HealthMagazine;
import com.korit.silverbutton.repository.HealthMagazineRepository;
import com.korit.silverbutton.service.HealthMagazineService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class HealthMagazineServiceImpl implements HealthMagazineService {
    private final HealthMagazineRepository healthMagazineRepository;

    // 헬스 매거진 저장
    @Override
    public ResponseDto<HealthMagazineResponseDto> postHealthMagazine(HealthMagazineRequestDto dto
    ) {
        HealthMagazineResponseDto data = null;
        String thumbnailImageUrl = dto.getThumbnailImageUrl();
        String title = dto.getTitle();
        String content = dto.getContent();
        String source = dto.getSource();
        LocalDateTime publishDate = LocalDateTime.now();
        int viewCount = dto.getViewCount();
        try {
            HealthMagazine healthMagazine = HealthMagazine.builder()
                    .thumbnailImageUrl(thumbnailImageUrl)
                    .title(title)
                    .content(content)
                    .source(source)
                    .publishedDate(publishDate)
                    .viewCount(viewCount)
                    .build();
            healthMagazineRepository.save(healthMagazine);
            data = new HealthMagazineResponseDto(healthMagazine);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_HEALTH_MAGAZINE_SUCCESS, data);
    }

    //   헬스 매거진 전체 조회
    @Override
    public ResponseDto<List<HealthMagazineResponseDto>> findAll(HealthMagazineRequestDto dto
    ) {
        List<HealthMagazineResponseDto> data = null;
        try {
            List<HealthMagazine> healthMagazines = healthMagazineRepository.findAll();
            if (healthMagazines.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.HEALTH_MAGAZINE_NOT_FOUND); //
            }
            data = healthMagazines.stream().map(HealthMagazineResponseDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_ALL_HEALTH_MAGAZINE_SUCCESS, data);
    }

    // 헬스매거진 단건 조회
    @Override
    public ResponseDto<HealthMagazineResponseDto> getHealthMagazineById(Long id
    ) {
        HealthMagazineResponseDto data = null;
        try {
            Optional<HealthMagazine> optionalHealthMagazine = healthMagazineRepository.getHealthMagazineById(id);
            if (optionalHealthMagazine.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.HEALTH_MAGAZINE_NOT_FOUND);
            }
            HealthMagazine healthMagazine = optionalHealthMagazine.get();
            healthMagazine.setViewCount(healthMagazine.getViewCount() + 1);
            healthMagazineRepository.save(healthMagazine);
            data = new HealthMagazineResponseDto(healthMagazine);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_HEALTH_MAGAZINE_SUCCESS, data);
    }

    // 헬스 매거진 삭제
    @Override
    public ResponseDto<Boolean> deleteHealthMagazineById(Long id
    ) {
        try {
            healthMagazineRepository.deleteHealthMagazineById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.DELETE_HEALTH_MAGAZINE_SUCCESS, true);
    }

    // 헬스 매거진 조회순 top5조회
    @Override
    public ResponseDto<List<HealthMagazineResponseDto>> findTop5ByOrderByViewCountDesc(
    ) {
        List<HealthMagazineResponseDto> data = null;
        try {
            List<HealthMagazine> healthMagazines = healthMagazineRepository.findTop5ByOrderByViewCountDesc();
            if (healthMagazines.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.HEALTH_MAGAZINE_NOT_FOUND);
            }
            data = healthMagazines.stream().map(HealthMagazineResponseDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_HEALTH_MAGAZINE_TOP5, data);
    }

    @Override
    public ResponseDto<List<HealthMagazineResponseDto>> findLatestByOrderByPublishedDateDesc(
    ) {
        List<HealthMagazineResponseDto> data = null;
        try {
            List<HealthMagazine> healthMagazines = healthMagazineRepository.findLatestByOrderByPublishedDateDesc();
            if (healthMagazines.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.HEALTH_MAGAZINE_NOT_FOUND);
            }
            data = healthMagazines.stream().map(HealthMagazineResponseDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_HEALTH_MAGAZINE_LATEST, data);
    }

    // 헬스 매거진 조회순 정렬
    @Override
    public ResponseDto<List<HealthMagazineResponseDto>> findAllByOrderByViewCountDesc(
    ) {
        List<HealthMagazineResponseDto> data = null;
        try {
            List<HealthMagazine> healthMagazines = healthMagazineRepository.findAllByOrderByViewCountDesc();
            if (healthMagazines.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.HEALTH_MAGAZINE_NOT_FOUND);
            }
            data = healthMagazines.stream().map(HealthMagazineResponseDto::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_HEALTH_MAGAZINE_DESC, data);
    }
}
