package kopo.poly.persistance.mapper;

import kopo.poly.dto.OcrDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IOcrMapper {
    List<OcrDTO> getOcrList() throws Exception;

    OcrDTO getOcrInfo(OcrDTO pDTO) throws Exception;

    void insertOcrInfo(OcrDTO pDTO) throws Exception;

    OcrDTO  getFilePath(OcrDTO pDTO) throws Exception;
}
