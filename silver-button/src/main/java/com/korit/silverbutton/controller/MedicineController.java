package com.korit.silverbutton.controller;

import com.korit.silverbutton.common.constant.ApiMappingPattern;
import com.korit.silverbutton.dto.ResponseDrugApi;
import com.korit.silverbutton.dto.medicine.response.OpenApiOneDto;
import com.korit.silverbutton.service.implement.MedicineServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiMappingPattern.MEDICINE)
public class MedicineController {

    private static final String MEDICINE_OPEN_API = "/open/data/one";

    private final MedicineServiceImpl medicineService;

    // 외부 API 사용 데이터 조회
    @GetMapping(MEDICINE_OPEN_API)
    public ResponseEntity<ResponseDrugApi<List<OpenApiOneDto>>> getMedicineList(@RequestParam int pageNo, @RequestParam int numOfRows){
        ResponseDrugApi<List<OpenApiOneDto>> medicines = medicineService.fetchMedicineData(pageNo, numOfRows);
        return ResponseEntity.status(HttpStatus.OK).body(medicines);
    }

    // 약품 이름이로 검색
//    @GetMapping(MEDICINE_NAME)
//    public ResponseEntity<ResponseDto<List<MedicineResponseDto>>> getDrugInfoByName(
//            @RequestParam String drugName
//    ){
//        ResponseDto<List<MedicineResponseDto>> medicines = medicineService.getDrugInfoByName(drugName);
//        HttpStatus status = medicines.isResult() ? HttpStatus.OK : HttpStatus.NOT_FOUND;
//        return ResponseEntity.status(status).body(medicines);
//    }

    // 약품 모양 색상 분할선으로 검색
//    @GetMapping(MEDICINE_OPTION)
//    public ResponseEntity<ResponseDto<List<MedicineResponseDto>>> getDrugInfoBySearchOption(
//            @RequestParam String shape, @RequestParam String color, @RequestParam String line
//    ){
//        ResponseDto<List<MedicineResponseDto>> medicines = medicineService.getDrugInfoBySearchOption(shape,color,line);
//        HttpStatus status = medicines.isResult() ? HttpStatus.NOT_FOUND : HttpStatus.OK;
//        return ResponseEntity.status(status).body(medicines);
//    }

    // 약품 전체 조회
//    @GetMapping(MEDICINE_LIST)
//    public ResponseEntity<List<MedicineResponseDto>> getMedicineList(){
//        ResponseDto<List<MedicineResponseDto>> result = medicineService.getMedicineList();
//        return ResponseEntity.status(HttpStatus.OK).body(result.getData());
//    }

    // 약품 모양으로 검색
//    @GetMapping(MEDICINE_SEARCH1)
//    public ResponseEntity<ResponseDto<List<MedicineResponseDto>>> searchMedicineByShape(
//            @RequestBody searchDrugRequestDto dto) {
//        ResponseDto<List<MedicineResponseDto>> response = medicineService.getSearchMedicine(dto);
//        HttpStatus status = response.isResult() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
//        return ResponseEntity.status(status).body(response);
//    }

    // 약품 이름으로 검색
//    @GetMapping(MEDICINE_NAME)
//    public ResponseEntity<ResponseDto<MedicineResponseDto>>
//    searchMedicineItem( @PathVariable String medicineName){
//        ResponseDto<MedicineResponseDto> result = medicineService.searchMedicineByName(medicineName);
//        return ResponseEntity.status(HttpStatus.OK).body(result);
//    }
}
