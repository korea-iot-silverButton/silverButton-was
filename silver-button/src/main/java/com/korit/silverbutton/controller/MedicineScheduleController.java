package com.korit.silverbutton.controller;


import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleResponseDto;

import com.korit.silverbutton.service.MedicineScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/medicine-schedule")
@RequiredArgsConstructor
public class MedicineScheduleController {

    private final MedicineScheduleService medicineScheduleService;

    // 약품 저장
    private static final String MEDICINE_POST = "/";
    private static final String MEDICINE_LIST_GET = "/";
    private static final String MEDICINE_GET = "/{itemSeq}";
    private static final String MEDICINE_DELETE = "/{itemSeq}";

    @PostMapping(MEDICINE_POST)
    public ResponseEntity<ResponseDto<MedicineScheduleResponseDto>> postMedicineByUserId(
            @AuthenticationPrincipal String userId,
            @RequestBody MedicineScheduleRequestDto dto
    ) {
        ResponseDto<MedicineScheduleResponseDto> response = medicineScheduleService.postMedicineByUserId(userId, dto);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(MEDICINE_LIST_GET)
    public ResponseEntity<ResponseDto<List<MedicineScheduleResponseDto>>> getMedicineAllByUserId(@AuthenticationPrincipal String userId) {
        ResponseDto<List<MedicineScheduleResponseDto>> response = medicineScheduleService.getMedicineAllByUserId(userId);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping(MEDICINE_GET)
    public ResponseEntity<ResponseDto<MedicineScheduleResponseDto>> getMedicineByUserIdAndItemSeq(@AuthenticationPrincipal String userId, @PathVariable String itemSeq) {
        ResponseDto<MedicineScheduleResponseDto> response = medicineScheduleService.getMedicineByUserIdAndItemSeq(userId, itemSeq);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

    @DeleteMapping(MEDICINE_DELETE)
    public ResponseEntity<Void> deleteMedicineByUserIdAndItemSeq(@AuthenticationPrincipal String userId, @PathVariable String itemSeq) {
        ResponseDto<MedicineScheduleResponseDto> response = medicineScheduleService.deleteMedicineByUserIdAndItemSeq(userId, itemSeq);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(null);
    }
}
