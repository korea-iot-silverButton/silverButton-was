package com.korit.silverbutton.controller;


import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.requset.MedicineSearchByNameRequestDto;
import com.korit.silverbutton.dto.medicine.response.MedicineResponseDto;
import com.korit.silverbutton.service.MedicineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
public class DrugApiController {
    private final MedicineService medicineService;

    @PostMapping("/search")
    public ResponseEntity<ResponseDto<List<MedicineResponseDto>>> searchMedicines(@RequestBody MedicineSearchByNameRequestDto requestDto, @RequestParam int pageNo, @RequestParam int numOfRows) {
        ResponseDto<List<MedicineResponseDto>> response = medicineService.searchMedicinesByName(requestDto,pageNo,numOfRows);
        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(response);
    }

}