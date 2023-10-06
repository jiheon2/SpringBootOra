package kopo.poly.controller;

import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Slf4j
@RequestMapping(value = "/user")
@RequiredArgsConstructor
@Controller
public class UserInfoController {
    private final IUserInfoService userInfoService;

    @GetMapping(value = "userRegForm")
    public String userRegForm() {
        log.info(this.getClass().getName() + ".user/userRegForm");

        return "/user/userRegForm";
    }

    @ResponseBody
    @PostMapping(value = "getUserIdExists")
    public UserInfoDTO getUserExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId"));

        log.info("userId" + userId);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserIdExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass() + ".getUserIdExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "getEmailExists")
    public UserInfoDTO getEmailExists(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getEmailExists Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getEmailExists(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass() + ".getEmailExists End!");

        return rDTO;
    }

    @ResponseBody
    @PostMapping(value = "insertUserInfo")
    public MsgDTO insertUserInfo(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo start!");

        int res = 0;
        String msg = "";
        MsgDTO dto = null;

        UserInfoDTO pDTO = null;

        try {
            String userId = CmmUtil.nvl(request.getParameter("userId"));
            String userName = CmmUtil.nvl(request.getParameter("userName"));
            String password = CmmUtil.nvl(request.getParameter("password"));
            String email = CmmUtil.nvl(request.getParameter("email"));
            String addr1 = CmmUtil.nvl(request.getParameter("addr1"));
            String addr2 = CmmUtil.nvl(request.getParameter("addr2"));

            log.info("userId" + userId);
            log.info("userName" + userName);
            log.info("password" + password);
            log.info("email" + email);
            log.info("addr1" + addr1);
            log.info("addr2" + addr2);

            pDTO = new UserInfoDTO();
            pDTO.setUserId(userId);
            pDTO.setUserName(userName);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));
            pDTO.setEmail(EncryptUtil.encAES128CBC(email));
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = userInfoService.insertUserInfo(pDTO);

            log.info("회원가입 결과(res) : " + res);

            if(res == 1) {
                msg = "회원가입되었습니다";
            } else if(res == 2) {
                msg = "이미 회원가입된 아이디 입니다.";
            } else {
                msg = "오류로 인해 회원가입이 실패하였습니다.";
            }

        } catch(Exception e) {
            msg = "실패하였습니다. : " + e;
            log.info(e.toString());
            e.printStackTrace();

        } finally {
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".insertUserInfo End!");
        }

        return dto;
    }

    @GetMapping(value = "userList")
    public String userList(HttpSession session, ModelMap model)
            throws Exception {

        // 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userList Start!");

        // 로그인된 사용자 아이디는 Session에 저장함
        // 교육용으로 아직 로그인을 구현하지 않았기 때문에 Session에 데이터를 저장하지 않았음
        // 추후 로그인을 구현할 것으로 가정하고, 공지사항 리스트 출력하는 함수에서 로그인 한 것처럼 Session 값을 생성함
        session.setAttribute("SESSION_USER_ID", "USER01");

        // 공지사항 리스트 조회하기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        List<UserInfoDTO> rList = Optional.ofNullable(userInfoService.getList())
                .orElseGet(ArrayList::new);

        for (UserInfoDTO userInfoDTO : rList) {
            String secretEmail = userInfoDTO.getEmail();
            String realEmail = EncryptUtil.decAES128CBC(secretEmail);
            userInfoDTO.setEmail(realEmail);
        }



//        List<NoticeDTO> rList = noticeService.getNoticeList();
//
//        if (rList == null) {
//            rList = new ArrayList<>();
//        }

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rList", rList);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info(this.getClass().getName() + ".userList End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeList.jsp
        return "user/userList";

    }

    @GetMapping(value = "userInfo")
    public String userListInfo(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".listInfo Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId")); // 공지글번호(PK)

        /*
         * ####################################################################################
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
         * ####################################################################################
         */
        log.info("userId : " + userId);

        /*
         * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
         */
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);

        // 공지사항 상세정보 가져오기
        // Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO)).orElseGet(UserInfoDTO::new);

        String secretEmail = rDTO.getEmail();
        log.info("rDTO secretEmail : " + secretEmail);
        String realEmail = EncryptUtil.decAES128CBC(secretEmail);
        log.info("rDTO realEmail : " + realEmail);
        rDTO.setEmail(realEmail);

        // 조회된 리스트 결과값 넣어주기
        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".noticeInfo End!");

        // 함수 처리가 끝나고 보여줄 JSP 파일명
        // webapp/WEB-INF/views/notice/noticeInfo.jsp
        return "user/userInfo";
    }


    // 로그인 화면으로 이동하는 코드
    @GetMapping(value="login")
    public String login() {
        log.info(this.getClass().getName() + ".user/login Start!");

        log.info(this.getClass().getName() + ".user/login End!");

        return "user/login";
    }

    @GetMapping(value="loginResult")
    public String loginResult() {
        log.info(this.getClass().getName() + ".user/loginResult Start!");

        log.info(this.getClass().getName() + ".user/loginResult End!");

        return "user/loginResult";
    }

    // 로그인 처리 및 결과 알려주는 코드
    @ResponseBody
    @PostMapping(value = "loginProc")
    public MsgDTO loginProc(HttpServletRequest request, HttpSession session) {

        log.info(this.getClass().getName() + ".loginProc Start!");

        int res = 0; // res : 로그인 결과처리를 저장하는 변수( 성공 : 1 / ID PW 불일치 : 0 / 시스템 오류 : 2 )
        String msg = ""; // 로그인 결과에 대한 메시지 전달 변수
        MsgDTO dto = null; // 결과 메시지 구조

        UserInfoDTO pDTO = null; // 웹에서 받는 정보 저장할 변수

        try {
            String userId = CmmUtil.nvl(request.getParameter("userId"));
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("userId : " + userId);
            log.info("password : " + password);

            // 웹에서 받는 정보를 저장할 변수를 메모리에 올림(new 연산자)
            pDTO = new UserInfoDTO();

            pDTO.setUserId(userId); // 아이디 넣고

            pDTO.setPassword(EncryptUtil.encHashSHA256(password)); // 비밀번호는 SHA255 써서 암호화 해서 넣음

            UserInfoDTO rDTO = userInfoService.getLogin(pDTO); // 아이디와 비밀번호 일치하는지 확인하기 위해 getLogin함수를 서비스에서 호출

            /**
             * 로그인에 성공했으면 회원 아이디 정보를 session에 저장
             *
             * 세션은 톰켓의 메모리에 존재, 웹사이트에 접속한 사람(연결된 객체)마다 메모리에 값을 올림
             *
             * ex) 100명이 웹사이트에 접속하면 100명 각자의 아이디를 메모리에 저장 > 과도한 세션은 메모리 부하 발생 > 서버다운
             *
             * 스프링에서 세션 사용하기
             * > 함수명의 파라미터에 HttpSession session이 존재해야 함
             *
             * url 전달이 필요없음 / jsp, Controller에서 쉽게 사용 가능
             **/

            if (CmmUtil.nvl(rDTO.getUserId()).length() > 0 ) { // 로그인이 성공 > length()가 0보다 크면 값이 있다는 것을 나타냄

                res = 1;

                msg = "로그인이 성공했습니다.";

                /*
                세션에 값 저장하기 > 추후 로그인여부 체크를 위함
                일반적으로 세션에 저장되는 키는 대문자로 입력 및 SS를 앞에 붙임
                 */
                session.setAttribute("SS_USER_ID", userId);
                session.setAttribute("SS_USER_NAME", CmmUtil.nvl(rDTO.getUserName()));

            } else {
                msg = "아이디와 비밀번호가 올바르지 않습니다.";
            }
        } catch (Exception e) { // 실패 시
            msg = "시스템 문제로 로그인에 실패했습니다.";
            res = 2;
            log.info(e.toString());
            e.printStackTrace();

        } finally { // 무조건 실행
            // 결과 메시지 전달
            dto = new MsgDTO();
            dto.setResult(res);
            dto.setMsg(msg);

            log.info(this.getClass().getName() + ".loginProc End!");
        }

        return dto;
    }

    @ResponseBody
    @PostMapping(value = "loginInfo")
    public String loginInfo(Model model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".loginInfo Start!");

        UserInfoDTO pDTO = null; // 웹에서 받는 정보 저장할 변수

        String userId = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));

        log.info("userId : " + userId);

        // 웹에서 받는 정보를 저장할 변수를 메모리에 올림(new 연산자)
        pDTO = new UserInfoDTO();

        pDTO.setUserId(userId); // 아이디 넣고

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getUserInfo(pDTO)).orElseGet(UserInfoDTO::new);

        if (userId == "") {

            return "/user/login";
        }

        model.addAttribute("rDTO", rDTO);


        log.info(this.getClass().getName() + ".loginInfo End!");


        return "/user/userInfo";
    }

    @GetMapping(value = "searchUserId")
    public String searchUserId() {

        log.info(this.getClass().getName() + ".user/searchUserId Start!");

        log.info(this.getClass().getName() + ".user/searchUserId End!");

        return "user/searchUserId";

    }

    /** ID 찾기 로직 수행 **/
    @PostMapping(value = "searchUserIdProc")
    public String searchUserIdProc(HttpServletRequest request, Model model) throws Exception {

        log.info(this.getClass().getName() + ".searchUserIdProc Start!");

        /* 웹에서 받는 정보를 String 변수에 저장 > bcz DTO에 저장하기위해 임시로 넣어두는 것 */

        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userName : " + userName);
        log.info("email : " + email);

        /* String 변수값 DTO에 저장하기 */

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email)); // 암호화 된걸 찾기 위해서 암호화한 것을 넣음

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO)).orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        log.info(this.getClass().getName() + ".searchUserIdProc End!");

        return "user/searchUserIdResult";
    }

    @GetMapping(value = "searchPassword")
    public String searchPassword(HttpSession session) {
        log.info(this.getClass().getName() + ".searchPassword Start!");

        // 강제 URL 입력 등 오는 경우가 있어 세션 삭제
        // 비밀번호 재생성하는 화면은 보안을 위해 생성한 NEW_PASSWORD 세션 삭제
        session.setAttribute("NEW_PASSWORD", "");
        session.removeAttribute("NEW_PASSWORD");

        log.info(this.getClass().getName() + ".searchPassword End!");

        return "user/searchPassword";
    }

    /** 비밀번호 찾기 로직 수행 **/
    // 아이디, 비밀번호, 이메일 일치하면 비밀번호 재발급 화면 이동
    @PostMapping(value = "searchPasswordProc")
    public String searchPasswordProc(HttpServletRequest request, Model model, HttpSession session) throws Exception {
        log.info(this.getClass().getName() + ".searchPasswordProc Start!");

        String userId = CmmUtil.nvl(request.getParameter("userId"));
        String userName = CmmUtil.nvl(request.getParameter("userName"));
        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("userId : " + userId);
        log.info("userName : " + userName);
        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUserId(userId);
        pDTO.setUserName(userName);
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.searchUserIdOrPasswordProc(pDTO)).orElseGet(UserInfoDTO::new);

        model.addAttribute("rDTO", rDTO);

        // 비밀번호 재생성 화면은 보안을 위해 반드시 세션이 존재해야 접속가능 하도록 구현
        // userId 값을 넣은 이유는 비밀번호 재설정하는 newPasswordProc 함수를 사용하기 위함
        session.setAttribute("NEW_PASSWORD", userId);

        log.info(this.getClass().getName() + ".searchPasswordProc End!");

        return "user/newPassword";
    }

    @PostMapping(value = "newPasswordProc")
    public String newPasswordProc(HttpServletRequest request, Model model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".newPasswordProc Start!");

        String msg = "";

        String newPassword = CmmUtil.nvl((String) session.getAttribute("NEW_PASSWORD"));

        if (newPassword.length() > 0) {
            String password = CmmUtil.nvl(request.getParameter("password"));

            log.info("password : " + password);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUserId(newPassword);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password));

            userInfoService.newPasswordProc(pDTO);

            session.setAttribute("NEW_PASSWORD", "");
            session.removeAttribute("NEW_PASSWORD");

            msg = "비밀번호가 재설정 되었습니다.";

        } else {
            msg = "비정상 접근입니다.";
        }

        model.addAttribute("msg", msg);

        log.info(this.getClass().getName() + ".newPasswordProc End!");

        return "user/newPasswordResult";
    }

    @ResponseBody
    @PostMapping(value = "getAuthNumber")
    public UserInfoDTO getAuthNumber(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getAuthNumber Start!");

        String email = CmmUtil.nvl(request.getParameter("email"));

        log.info("email : " + email);

        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setEmail(EncryptUtil.encAES128CBC(email));

        UserInfoDTO rDTO = Optional.ofNullable(userInfoService.getAuthNumber(pDTO)).orElseGet(UserInfoDTO::new);

        log.info(this.getClass() + ".getAuthNumber End!");

        return rDTO;
    }
}
