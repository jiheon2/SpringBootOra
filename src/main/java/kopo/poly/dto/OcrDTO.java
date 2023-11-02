package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrDTO {
    private String seq;
    private String saveFilePath;
    private String saveFileName;
    private String textFormImage;
    private String orgFileName;
    private String ext;
    private String ocrText;
    private String regId;
    private String regDt;
    private String chgId;
    private String chgDt;
}
