package kopo.poly.service.impl;

import kopo.poly.dto.OcrDTO;
import kopo.poly.persistance.mapper.IOcrMapper;
import kopo.poly.service.IOcrService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrService implements IOcrService {

    private final IOcrMapper ocrMapper;

    @Value("${orc.model.data}")
    private String ocrModel;

    /**
     * 이미지 파일로 부터 문자 읽어오기
     * @param pDTO
     * @return pDTO
     * @throws Exception
     */
    @Override
    public OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getReadforImageText start!");

        File imageFile = new File(CmmUtil.nvl(pDTO.getSaveFilePath()) + "//" + CmmUtil.nvl(pDTO.getSaveFileName())); // 컨트롤러에서 업로드한 파일

        ITesseract instance = new Tesseract(); // OCR 기술 사용을 위한 Tesseract 플랫폼 객체 생성

        instance.setDatapath(ocrModel); // OCR 분석에 필요한 기준 데이터 / 저장경로는 물리경로 사용

        instance.setLanguage("kor"); // 한국어 학습 데이터 선택
        // instance.setLanguage("eng"); // 영어

        String result = instance.doOCR(imageFile); // 이미지 파일로 부터 텍스트 읽기

        pDTO.setTextFormImage(result); // 읽은 글자 DTO에 저장

        log.info("result : " + result);

        log.info(this.getClass().getName() + ".getReadforImageText End!");

        return pDTO;
    }

    @Override
    public List<OcrDTO> getOcrList() throws Exception {

        log.info(this.getClass().getName() + ".getOcrList start!");

        return ocrMapper.getOcrList();
    }
    @Transactional
    @Override
    public OcrDTO getOcrInfo(OcrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getOcrInfo start!");

        return ocrMapper.getOcrInfo(pDTO);
    }
    @Transactional
    @Override
    public void insertOcrInfo(OcrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".InsertOcrInfo Start!");

        ocrMapper.insertOcrInfo(pDTO);
    }

    @Transactional

    @Override
    public OcrDTO getFilePath(OcrDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getFilePath Start!");

        return ocrMapper.getFilePath(pDTO);
    }
}
