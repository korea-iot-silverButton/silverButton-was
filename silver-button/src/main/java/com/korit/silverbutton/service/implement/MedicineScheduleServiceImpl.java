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
    private final MedicineScheduleRepository medicineScheduleRepository; //

    // 약품 정보 저장
    @Override
    public ResponseDto<MedicineScheduleResponseDto> postMedicineByUserId(String userId, MedicineScheduleRequestDto dto) {
        MedicineScheduleResponseDto data = null;
        String itemName = dto.getItemName();
        Long itemSeq = dto.getItemSeq();
        String useMethodQesitm = dto.getUseMethodQesitm();
        String atpnQesitm = dto.getAtpnQesitm();
        String seQesitm = dto.getSeQesitm();
        String depositMethodQesitm = dto.getDepositMethodQesitm();
        String intrcQesitm = dto.getIntrcQesitm();

        try {
            MedicineSchedule medicineSchedule = MedicineSchedule.builder()
                    .itemName(itemName)
                    .itemSeq(itemSeq)
                    .useMethodQesitm(useMethodQesitm)
                    .atpnQesitm(atpnQesitm)
                    .seQesitm(seQesitm)
                    .depositMethodQesitm(depositMethodQesitm)
                    .intrcQesitm(intrcQesitm)
                    .userId(userId)
                    .build();
            medicineScheduleRepository.save(medicineSchedule);

            data = new MedicineScheduleResponseDto(medicineSchedule);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 약품 정보 전체 조회
    @Override
    public ResponseDto<List<MedicineScheduleResponseDto>> getMedicineAllByUserId(String userId) {
        List<MedicineScheduleResponseDto> data = null;

        try {
            Optional<List<MedicineSchedule>> optionalMedicineSchedule = medicineScheduleRepository.getMedicineAllByUserId(userId);
            if (optionalMedicineSchedule.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
            }
            List<MedicineSchedule> medicineSchedule = optionalMedicineSchedule.get();
            data = medicineSchedule.stream()
                    .map(MedicineScheduleResponseDto::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    // 약품 정보 단건 조회
    @Override
    public ResponseDto<MedicineScheduleResponseDto> getMedicineByUserIdAndItemSeq(String userId, Long itemSeq) {
        MedicineScheduleResponseDto data = null;

        try {
            Optional<MedicineSchedule> optionalMedicineSchedule = medicineScheduleRepository.getMedicineByUserIdAndItemSeq(userId, itemSeq);
            if (optionalMedicineSchedule.isEmpty()) {
                return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
            }
            MedicineSchedule medicineSchedule = optionalMedicineSchedule.get();
            data = new MedicineScheduleResponseDto(medicineSchedule);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }


    // 약품 정보 삭제
    @Override
    public ResponseDto<Boolean> deleteMedicineByUserIdAndItemSeq(String userId, Long itemSeq) {

        try {
            medicineScheduleRepository.deleteMedicineByUserIdAndItemSeq(userId, itemSeq);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, true);
    }
}
