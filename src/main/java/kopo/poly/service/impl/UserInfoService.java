package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.IUserInfoMapper;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserInfoService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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


}
