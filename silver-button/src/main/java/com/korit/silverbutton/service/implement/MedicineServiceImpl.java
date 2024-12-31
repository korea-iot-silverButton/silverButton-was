package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.dto.ResponseDrugApi;
import com.korit.silverbutton.dto.medicine.response.OpenApiOneDto;
import com.korit.silverbutton.repository.MedicineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class MedicineServiceImpl {

    @Value("${api.medicine.serviceKey}")
    private String serviceKey;

    @Value("${api.medicine.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;
    private final MedicineRepository medicineRepository;

//    public ResponseDto<List<MedicineResponseDto>> getDrugInfoByName(String name) {
//        try {
//            List<MedicineResponseDto> data = null;
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
//        } catch (Exception e) {
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//    }

//
//    public ResponseDto<List<MedicineResponseDto>> getDrugInfoBySearchOption(String shape, String color, String line) {
//        try {
//            List<MedicineResponseDto> data = medicineRepository.findAll().stream()
//                    .filter(medicine -> medicine.getColorClass1().equals(color))
//                    .filter(medicine -> medicine.getDrugShape().equals(shape))
//                    .filter(medicine -> medicine.getLineFront().equals(line))
//                    .map(MedicineResponseDto::new)
//                    .toList();
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
//        } catch (Exception e) {
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//    }


    public ResponseDrugApi<List<OpenApiOneDto>> fetchMedicineData(int pageNo, int numOfRows) {
        ResponseDrugApi<List<OpenApiOneDto>> data = null;

        String uri = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .queryParam("type", "json")
                .toUriString();

        webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(serviceKey);
        System.out.println(apiUrl);
        System.out.println(pageNo);
        System.out.println(numOfRows);

        return data;
    }
}


// 약품 전체 조회
//    public ResponseDto<List<MedicineResponseDto>> getMedicineList() {
//        try {
//            List<MedicineResponseDto> data = medicineRepository.findAll().stream()
//                    .map(MedicineResponseDto::new)
//                    .toList();
//
//            return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
//        } catch (Exception e) {
//            logError(e, "Failed to fetch medicine list");
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//    }

//    public ResponseDto<List<MedicineResponseDto>> getSearchMedicine(searchDrugRequestDto dto) {
//        List<MedicineResponseDto> data = null;
//
//        try {
//            // findBy 메소드 사용
//            List<Medicine> medicineList = medicineRepository.findByDrugShapeAndColorClass1AndLineFrontAndItemName(
//                    dto.getShape(), dto.getColor(), dto.getLine(), dto.getName());
//
//            // 검색된 데이터를 DTO로 변환
//            data = medicineList.stream()
//                    .map(MedicineResponseDto::new)
//                    .collect(Collectors.toList());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
//        }
//
//        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
//    }
//}

// 약품 이름으로 검색
//    public ResponseDto<MedicineResponseDto> searchMedicineByName(String medicineName) {
//
//    }





