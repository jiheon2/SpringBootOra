package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailInfoDTO {
    private int mailSeq;
    private String toMail;
    private String title;
    private String contents;
    private String sendTime;

}
