package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.response.MedicineApi1ResponseDto;
import com.korit.silverbutton.dto.medicine.response.MedicineApi2ResponseDto;
import com.korit.silverbutton.dto.medicine.requset.MedicineSearchByNameRequestDto;
import com.korit.silverbutton.dto.medicine.response.MedicineResponseDto;
import com.korit.silverbutton.service.MedicineService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class MedicineServiceImpl implements MedicineService {
    @Value("${drug.api.service-key}")
    String serviceKey;

    private final WebClient webClient;

    public MedicineServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public ResponseDto<List<MedicineResponseDto>> searchMedicinesByName(MedicineSearchByNameRequestDto dto,
                                                                        int pageNo,
                                                                        int numOfRows) {
        List<MedicineResponseDto> data = new ArrayList<>();
        String medicineName = dto.getMedicineName();

        String encodedServiceKey = URLEncoder.encode(serviceKey, StandardCharsets.UTF_8);

        try {
            var firstApiResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01")
                            .queryParam("serviceKey", "jptKXkEhoWS2pwVQ34adwBGaLMbSQxl8jipaqrcP3oFbUD%2BVSG73q0mvxhSxJ46NK3v%2BsGLTPy0bH0oTQmuSdQ%3D%3D")
                            .queryParam("type", "json")
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .queryParam("ITEM_NAME", medicineName)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            var secondApiResponse = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("apis.data.go.kr")
                            .path("/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList")
                            .queryParam("ServiceKey", "jptKXkEhoWS2pwVQ34adwBGaLMbSQxl8jipaqrcP3oFbUD%2BVSG73q0mvxhSxJ46NK3v%2BsGLTPy0bH0oTQmuSdQ%3D%3D")
                            .queryParam("type", "json")
                            .queryParam("pageNo", pageNo)
                            .queryParam("numOfRows", numOfRows)
                            .queryParam("itemName", medicineName)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            System.out.println("Service Key: " + serviceKey);

            data.add(new MedicineApi1ResponseDto("API1", firstApiResponse));
            data.add(new MedicineApi2ResponseDto("API2", secondApiResponse));

        } catch (Exception e) {
            e.printStackTrace();
            ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }

        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }
}