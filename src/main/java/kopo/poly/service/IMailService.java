package kopo.poly.service;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MailInfoDTO;

import java.util.List;

public interface IMailService {
    int doSendMail(MailDTO pDTO); // 메일 발송
    // throws Exception; 를 작성한 곳에 오류를 다시 던져줌
    // Service는 Controller가 호출하는데 throws Exception을 작성하면 Controller에게 오류를 다시 전달
    // 작성하지않으면 Service가 스스로 에러를 처리한다는 뜻
    List<MailInfoDTO> getMailList() throws Exception;

    void insertMailInfo(MailInfoDTO pDTO) throws Exception;
}
