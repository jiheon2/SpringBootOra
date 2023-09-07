package kopo.poly.controller;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MailInfoDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
@Controller
public class MailController {
    private final IMailService mailService;

    // 메일 발송하기 폼
    @GetMapping(value = "mailForm")
    public String mailForm() throws Exception {

        log.info(this.getClass().getName() + "mailFrom start");

        return "/mail/mailForm";
    }

    @ResponseBody
    @PostMapping(value = "sendMail")
    public MsgDTO sendMail(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".sendMail start");

        String msg = ""; // 발송 결과 메시지

        // 웹 url로부터 전달받는 값
        String toMail = CmmUtil.nvl(request.getParameter("toMail"));
        String title = CmmUtil.nvl(request.getParameter("title"));
        String contents = CmmUtil.nvl(request.getParameter("contents"));

        // 값 확인하기
        log.info("toMail : " + toMail);
        log.info("title : " + title);
        log.info("contents : " + contents);

        // 메일 발송할 DTO 객체 생성
        MailDTO pDTO = new MailDTO();

        // 웹에서 받은 값 DTO에 넣기
        pDTO.setToMail(toMail);
        pDTO.setTitle(title);
        pDTO.setContents(contents);

        // 메일 발송하기
        int res = mailService.doSendMail(pDTO);

        MailInfoDTO rDTO = new MailInfoDTO();
        rDTO.setTitle(pDTO.getTitle());
        rDTO.setToMail(pDTO.getToMail());
        rDTO.setContents(pDTO.getContents());

        mailService.insertMailInfo(rDTO);

        if (res == 1) {
            msg = "메일 발송 완료";
        } else {
            msg = "메일 발송 실패";
        }

        log.info(msg);

        // 결과 메시지 전달
        MsgDTO dto = new MsgDTO();
        dto.setMsg(msg);

        log.info(this.getClass().getName() + ".sendMail end");

        return dto;
    }

    @GetMapping(value = "mailList")
    public String noticeList(ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".mailList Start!");

        // 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<MailInfoDTO> rList = Optional.ofNullable(mailService.getMailList())
                .orElseGet(ArrayList::new);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".mailList End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeList.jsp
        return "mail/mailList";

    }
}
