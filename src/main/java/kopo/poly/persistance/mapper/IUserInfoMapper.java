package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IUserInfoMapper {
    int insertUserInfo(UserInfoDTO pDTO) throws Exception; // 회원가입

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception; // 아이디 중복체크(DB조회)

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception; // 이메일 중복체크(DB조회)

    List<UserInfoDTO> getList() throws Exception;

    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;
}
