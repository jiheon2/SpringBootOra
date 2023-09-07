package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MailInfoDTO;
import kopo.poly.persistance.mapper.IMailMapper;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService implements IMailService {
    private final JavaMailSender mailSender;

    private final IMailMapper mailMapper;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public int doSendMail(MailDTO pDTO) {

        // 시작 로그
        log.info(this.getClass().getName() + "do SendMail start");

        // 메일발송 성공여부(성공:1 / 실패:0)
        int res = 1;

        // 받은 DTO로 데이터 가져오기
        if (pDTO == null) {
            pDTO = new MailDTO();
        }

        String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는 사람
        String title = CmmUtil.nvl(pDTO.getTitle()); // 제목
        String contents = CmmUtil.nvl(pDTO.getContents()); // 내용


        log.info("toMail : " + toMail);
        log.info("title : " + title);
        log.info("contents : " + contents);

        // Mail발송 구조(파일첨부 가능)
        MimeMessage message = mailSender.createMimeMessage();

        // 메일 발송 메시지 구조를 쉽게 생성하게 해주는 객체
        MimeMessageHelper messageHelper = new MimeMessageHelper(message,"UTF-8");

        try {

            messageHelper.setTo(toMail);
            messageHelper.setFrom(fromMail);
            messageHelper.setSubject(title);
            messageHelper.setText(contents);

            mailSender.send(message);

        } catch (Exception e) {
            res = 0; // 메일발송 실패시 0
            log.info("[ERROR] " + this.getClass().getName() + ".doSendMail : " + e);
        }

        log.info(this.getClass().getName() + ".doSendMail end");

        return res;
    }

    @Override
    public List<MailInfoDTO> getMailList() throws Exception {

        log.info(this.getClass().getName() + ".getNoticeList start!");

        return mailMapper.getMailList();

    }

    @Override
    public void insertMailInfo(MailInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".InsertMailInfo start!");

        mailMapper.insertMailInfo(pDTO);
    }
}
