package com.korit.silverbutton.controller;

import com.korit.silverbutton.dto.HealthMagazine.request.HealthMagazineRequestDto;
import com.korit.silverbutton.dto.HealthMagazine.response.HealthMagazineResponseDto;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.service.HealthMagazineService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.korit.silverbutton.common.constant.ApiMappingPattern.HEALTH_MAGAZINE;

@RestController
@RequestMapping(HEALTH_MAGAZINE)
@RequiredArgsConstructor
public class HealthMagazineController {


    private static final String HEALTH_MAGAZINE_POST = "/";
    private static final String HEALTH_MAGAZINE_GET = "/{id}";
    private static final String HEALTH_MAGAZINE_LIST_GET = "/";
    private static final String HEALTH_MAGAZINE_DELETE = "/{id}";
    private static final String HEALTH_MAGAZINE_GET_TOP5 = "/top5";
    private static final String HEALTH_MAGAZINE_GET_LATEST = "/latest";
    private static final String HEALTH_MAGAZINE_GET_DESC = "/desc";
    private final HealthMagazineService healthMagazineService;

    // 헬스 매거진 생성
    @PostMapping(HEALTH_MAGAZINE_POST)
    public ResponseEntity<ResponseDto<HealthMagazineResponseDto>> postHealthMagazine(@RequestBody HealthMagazineRequestDto dto
    ) {
        ResponseDto<HealthMagazineResponseDto> response = healthMagazineService.postHealthMagazine(dto);
        HttpStatus status = response.isResult() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    //     헬스 매거진 전체 조회
    @GetMapping(HEALTH_MAGAZINE_LIST_GET)
    public ResponseEntity<ResponseDto<List<HealthMagazineResponseDto>>> findAll(HealthMagazineRequestDto dto
    ) {
        ResponseDto<List<HealthMagazineResponseDto>> response = healthMagazineService.findAll(dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 헬스 매거진 단건 조회
    @GetMapping(HEALTH_MAGAZINE_GET)
    public ResponseEntity<ResponseDto<HealthMagazineResponseDto>> getHealthMagazineById(@PathVariable Long id
    ) {
        ResponseDto<HealthMagazineResponseDto> response = healthMagazineService.getHealthMagazineById(id);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 헬스 매거진 삭제
    @DeleteMapping(HEALTH_MAGAZINE_DELETE)
    public ResponseEntity<ResponseDto<Boolean>> deleteHealthMagazineById(@PathVariable Long id
    ) {
        ResponseDto<Boolean> response = healthMagazineService.deleteHealthMagazineById(id);
        HttpStatus status = response.isResult() ? HttpStatus.CREATED : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 헬스 매거진 조회순 top5 조회
    @GetMapping(HEALTH_MAGAZINE_GET_TOP5)
    public ResponseEntity<ResponseDto<List<HealthMagazineResponseDto>>> findTop5ByOrderByViewCountDesc(
    ) {
        ResponseDto<List<HealthMagazineResponseDto>> response = healthMagazineService.findTop5ByOrderByViewCountDesc();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 헬스 매거진 작성일자 최신순으로 조회
    @GetMapping(HEALTH_MAGAZINE_GET_LATEST)
    public ResponseEntity<ResponseDto<List<HealthMagazineResponseDto>>> findLatestByOrderByPublishedDateDesc(
    ) {
        ResponseDto<List<HealthMagazineResponseDto>> response = healthMagazineService.findLatestByOrderByPublishedDateDesc();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 헬스 매거진 조회순 정렬
    @GetMapping(HEALTH_MAGAZINE_GET_DESC)
    public ResponseEntity<ResponseDto<List<HealthMagazineResponseDto>>> findDescByOrderByViewCountDesc(
    ) {
        ResponseDto<List<HealthMagazineResponseDto>> response = healthMagazineService.findAllByOrderByViewCountDesc();
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }
}