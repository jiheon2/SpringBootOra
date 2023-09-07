package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailDTO {
    String toMail; // 받는사람
    String title; // 제목
    String contents; // 내용
}
