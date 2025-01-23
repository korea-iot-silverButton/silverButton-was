package com.korit.silverbutton.service.implement;

import com.korit.silverbutton.common.constant.ResponseMessage;
import com.korit.silverbutton.dto.ResponseDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleRequestDto;
import com.korit.silverbutton.dto.medicine.MedicineScheduleResponseDto;
import com.korit.silverbutton.entity.MedicineSchedule;
import com.korit.silverbutton.repository.MedicineScheduleRepository;
import com.korit.silverbutton.service.MedicineScheduleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MedicineScheduleServiceImpl implements MedicineScheduleService {
    private final MedicineScheduleRepository medicineScheduleRepository;

    // 약품 정보 저장
    @Override
    public ResponseDto<MedicineScheduleResponseDto> postMedicineByUserId(Long id, MedicineScheduleRequestDto dto
    ) {
        MedicineScheduleResponseDto data = null;
        String userId = dto.getUserId();
        String itemName = dto.getItemName();
        String efcyQesitm = dto.getEfcyQesitm();
        Long itemSeq = dto.getItemSeq();
        String useMethodQesitm = dto.getUseMethodQesitm();
        String atpnQesitm = dto.getAtpnQesitm();
        String seQesitm = dto.getSeQesitm();
        String depositMethodQesitm = dto.getDepositMethodQesitm();
        String intrcQesitm = dto.getIntrcQesitm();
        String medicineImage = dto.getMedicineImage();
        try {
            MedicineSchedule medicineSchedule = MedicineSchedule.builder()
                    .itemName(itemName)
                    .efcyQesitm(efcyQesitm)
                    .itemSeq(itemSeq)
                    .useMethodQesitm(useMethodQesitm)
                    .atpnQesitm(atpnQesitm)
                    .seQesitm(seQesitm)
                    .depositMethodQesitm(depositMethodQesitm)
                    .intrcQesitm(intrcQesitm)
                    .userId(userId)
                    .medicineImage(medicineImage)
                    .build();
            medicineScheduleRepository.save(medicineSchedule);
            data = new MedicineScheduleResponseDto(medicineSchedule);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.POST_MEDICINE_SCHEDULE_SUCCESS, data);
    }

    // 약품 정보 전체 조회
    @Override
    public ResponseDto<List<MedicineScheduleResponseDto>> getMedicineAllByUserId(String userId
    ) {
        List<MedicineScheduleResponseDto> data = null;
        try {
            Optional<List<MedicineSchedule>> optionalMedicineSchedule = medicineScheduleRepository.getMedicineAllByUserId(userId);
            System.out.println(userId);
            if (optionalMedicineSchedule.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.MEDICINE_SCHEDULE_NOT_FOUND);
            }
            List<MedicineSchedule> medicineSchedule = optionalMedicineSchedule.get();
            data = medicineSchedule.stream()
                    .map(MedicineScheduleResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_ALL_MEDICINE_SCHEDULE_SUCCESS, data);
    }

    // 약품 정보 단건 조회
    @Override
    public ResponseDto<MedicineScheduleResponseDto> getMedicineByUserIdAndItemSeq(String userId, Long itemSeq
    ) {
        MedicineScheduleResponseDto data = null;
        try {
            Optional<MedicineSchedule> optionalMedicineSchedule = medicineScheduleRepository.getMedicineByUserIdAndItemSeq(userId, itemSeq);
            if (optionalMedicineSchedule.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.MEDICINE_SCHEDULE_NOT_FOUND);
            }
            MedicineSchedule medicineSchedule = optionalMedicineSchedule.get();
            data = new MedicineScheduleResponseDto(medicineSchedule);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.GET_MEDICINE_SCHEDULE_SUCCESS, data);
    }



    // 약품 정보 삭제
    @Override
    public ResponseDto<Boolean> deleteMedicineByUserIdAndItemSeq(String userId, Long itemSeq
    ) {
        try {
            medicineScheduleRepository.deleteMedicineByUserIdAndItemSeq(userId, itemSeq);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.DELETE_MEDICINE_SCHEDULE_SUCCESS, true);
    }
}
