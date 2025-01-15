package com.korit.silverbutton.controller;

import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.request.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicine.response.MedicineScheduleResponseDto;
import com.korit.silverbutton.service.MedicineScheduleService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.korit.silverbutton.common.constant.ApiMappingPattern.MEDICINE;

@RestController
@RequestMapping(MEDICINE)
@RequiredArgsConstructor
public class MedicineScheduleController {

    private static final String MEDICINE_POST = "/";
    private static final String MEDICINE_LIST_GET = "/";
    private static final String MEDICINE_GET = "/{itemSeq}";
    private static final String MEDICINE_DELETE = "/{itemSeq}";

    private final MedicineScheduleService medicineScheduleService;

    // 메디슨 스케줄 생성
    @PostMapping(MEDICINE_POST)
    public ResponseEntity<ResponseDto<MedicineScheduleResponseDto>> postMedicineByUserId(
            @AuthenticationPrincipal String userId,
            @RequestBody MedicineScheduleRequestDto dto
    ) {
        ResponseDto<MedicineScheduleResponseDto> response = medicineScheduleService.postMedicineByUserId(userId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    // 메디슨 스케줄 전체 조회
    @GetMapping(MEDICINE_LIST_GET)
    public ResponseEntity<ResponseDto<List<MedicineScheduleResponseDto>>> getMedicineAllByUserId(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String userId = userDetails.getUsername();
        ResponseDto<List<MedicineScheduleResponseDto>> response = medicineScheduleService.getMedicineAllByUserId(userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 메디슨 스케줄 단건 조회
    @GetMapping(MEDICINE_GET)
    public ResponseEntity<ResponseDto<MedicineScheduleResponseDto>> getMedicineByUserIdAndItemSeq(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long itemSeq
    ) {
        String userId = userDetails.getUsername();
        ResponseDto<MedicineScheduleResponseDto> response = medicineScheduleService.getMedicineByUserIdAndItemSeq(userId, itemSeq);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    // 메디슨 스케줄 삭제
    @DeleteMapping(MEDICINE_DELETE)
    public ResponseEntity<ResponseDto<Boolean>> deleteMedicineByUserIdAndItemSeq(@AuthenticationPrincipal String userId, @PathVariable Long itemSeq
    ) {
        ResponseDto<Boolean> response = medicineScheduleService.deleteMedicineByUserIdAndItemSeq(userId, itemSeq);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }



}