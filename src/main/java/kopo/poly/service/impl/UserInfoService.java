package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.IUserInfoMapper;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoService implements IUserInfoService {
    private final IUserInfoMapper userInfoMapper; // Mapper 가져오기

    private  final IMailService mailService; // 메일서비스 가져오기

    @Override
    public UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserIdExists Start!");

        UserInfoDTO rDTO = userInfoMapper.getUserIdExists(pDTO); // 중복체크 쿼리 실행

        log.info(this.getClass().getName() + ".getUserIdExists End!");

        return rDTO;
    }

    @Override
    public UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".emailAuth Start!");

        UserInfoDTO rDTO = userInfoMapper.getEmailExists(pDTO); // 중복체크 쿼리 실행

        String existsYn = CmmUtil.nvl(rDTO.getExistsYn());

        log.info("existsYn : " + existsYn);

        if (existsYn.equals("N")) {

            // 6자리 랜덤번호 생성
            int authNumber = ThreadLocalRandom.current().nextInt(100000, 1000000);

            log.info("authNumber : " + authNumber);

            // 인증번호 발송 로직
            MailDTO dto = new MailDTO();

            dto.setTitle("이메일 중복 확인 인증번호 발송 메일");
            dto.setContents("인증번호는 " + authNumber + " 입니다.");
            dto.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            mailService.doSendMail(dto);

            dto = null;

            // 인증번호를 결과값에 넣어주기
            rDTO.setAuthNumber(authNumber);
        }

        log.info(this.getClass().getName() + ".emailAuth End!");

        return rDTO;
    }

    @Override
    public int insertUserInfo(UserInfoDTO pDTO) throws Exception {
        log.info(this.getClass().getName() + ".insertUserInfo Start!");

        // 회원가입 성공 : 1, 아이디 중복으로 인한 가입 취소 : 2, 기타 에러 발생 : 0
        int res = 0;

        // 회원가입
        int success = userInfoMapper.insertUserInfo(pDTO);

        // 회원가입 성공했다면
        if (success > 0) {
            res = 1;

            MailDTO mDTO = new MailDTO();

            // 회원정보화면에서 입력받은 이메일 변수(암호화되어 넘어오기 때문에 복호화 진행)
            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));

            // 제목
            mDTO.setTitle("회원가입을 축하드립니다!");

            // 내용
            mDTO.setContents(CmmUtil.nvl(pDTO.getUserName()) + "님의 회원가입을 진심으로 축하드립니다.");

            // 메일 발송
            mailService.doSendMail(mDTO);

        } else {
            res = 0;
        }

        log.info(this.getClass().getName() + ".insertUserInfo End!");

        return res;
    }

    @Override
    public List<UserInfoDTO> getList() throws Exception {

        log.info(this.getClass().getName() + ".getNoticeList start!");

        return userInfoMapper.getList();

    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserInfo start!");

        return userInfoMapper.getUserInfo(pDTO);

    }

    /**
     * 로그인을 위한 아이디와 비밀번호 일치여부 확인하는 코드
     *
     * @param pDTO 로그인을 위한 회원아이디, 비밀번호
     * @return 로그인 된 회원아이디 정보
     * @throws Exception
     */
    @Override
    public UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getLogin Start!");

        // ID PW 일치여부 확인하기 위한 mapper 호출
        // userInfoMapper.getLogin(pDTO)의 함수결과가 Null이면 UserInfoDTO 메모리에 올리는 코드
        UserInfoDTO rDTO = Optional.ofNullable(userInfoMapper.getLogin(pDTO)).orElseGet(UserInfoDTO::new);

        /*
         * userInfoMapper로 부터 SELECT 쿼리의 결과로 회원아이디를 받아왔다면 로그인 성공
         *
         * DTO의 변수에 값이 있는지 확인하는 방법 중 처리속도에서 가장 좋은 방법은 DTO의 길이를 확인하는 방법
         * .length()를 통해 회원아이디의 글자수를 가져와 0보다 큰지 비교
         * 0보다 크다는 것은 값이 존재한다는 것을 의미
         */
        if (CmmUtil.nvl(rDTO.getUserId()).length() > 0 ) {

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(rDTO.getEmail()))); // 복호화 코드

            mDTO.setTitle("로그인 알림"); // 제목

            mDTO.setContents(DateUtil.getDateTime("yyyy.MM.dd hh:mm:ss") + "에 " + CmmUtil.nvl(rDTO.getUserName()) + "님이 로그인하셨습니다."); // 내용

            mailService.doSendMail(mDTO); // 메일 보내는 서비스 로직

        }

        log.info(this.getClass().getName() + ".getLogin End!");

        return rDTO;
    }
}
