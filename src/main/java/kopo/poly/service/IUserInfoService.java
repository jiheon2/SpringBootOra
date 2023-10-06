package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

import java.util.List;

public interface IUserInfoService {

    UserInfoDTO getUserIdExists(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getEmailExists(UserInfoDTO pDTO) throws Exception;

    int insertUserInfo(UserInfoDTO pDTO) throws Exception;

    List<UserInfoDTO> getList() throws Exception;

    UserInfoDTO getUserInfo(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getLogin(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO searchUserIdOrPasswordProc(UserInfoDTO pDTO) throws Exception;

    int newPasswordProc(UserInfoDTO pDTO) throws Exception;

    UserInfoDTO getAuthNumber(UserInfoDTO pDTO) throws Exception;
}
