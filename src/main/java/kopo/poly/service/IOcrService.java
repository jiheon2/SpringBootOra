package kopo.poly.service;

import kopo.poly.dto.OcrDTO;

import java.util.List;

public interface IOcrService {
    OcrDTO getReadforImageText(OcrDTO pDTO) throws Exception; // 이미지 파일에서 문자 읽어오기

    List<OcrDTO> getOcrList() throws Exception;

    OcrDTO getOcrInfo(OcrDTO pDTO) throws Exception;

    void insertOcrInfo(OcrDTO pDTO) throws Exception;

    OcrDTO getFilePath(OcrDTO pDTO) throws Exception;
}
