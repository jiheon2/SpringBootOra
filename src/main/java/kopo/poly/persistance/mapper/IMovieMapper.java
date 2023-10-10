package kopo.poly.persistance.mapper;

import kopo.poly.dto.MovieDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IMovieMapper {
    int insertMovieInfo(MovieDTO pDTO) throws Exception; // 수집된 영화 순위 DB에 등록

    int deleteMovieInfo(MovieDTO pDTO) throws Exception; // 수집된 영화 순위 DB에서 삭제

    List<MovieDTO> getMovieInfo(MovieDTO pDTO) throws Exception; // 수집된 내용 조회
}
