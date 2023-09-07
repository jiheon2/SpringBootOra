package kopo.poly.persistance.mapper;

import kopo.poly.dto.MailInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMailMapper {
    List<MailInfoDTO> getMailList() throws Exception;

    void insertMailInfo(MailInfoDTO pDTO) throws Exception;
}
