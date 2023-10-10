package kopo.poly.service.impl;

import kopo.poly.dto.MovieDTO;
import kopo.poly.persistance.mapper.IMovieMapper;
import kopo.poly.service.IMovieService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService implements IMovieService {
    private final IMovieMapper movieMapper;

    @Transactional
    @Override
    public int collectMovieRank() throws Exception {

        log.info(this.getClass().getName() + ".collectMovieRank Start!");

        String collectTime = DateUtil.getDateTime("yyyyMMdd"); // 수집날짜 =  오늘날짜

        MovieDTO pDTO = new MovieDTO();
        pDTO.setCollectTime(collectTime);

        movieMapper.deleteMovieInfo(pDTO); // 기존에 수집된 영화데이터 삭제

        pDTO = null; // 기존 영화 삭제후, pDTO 값 제거

        int res = 0; // 크롤링 결과 (0보다 크면 성공)

        String url = "http://www.cgv.co.kr/movies/"; // CGV 영화순위 정보 가져올 사이트 주소

        Document doc = null; // JSOUP 라이브러리를 통해 사이트에 접속되면, 그 사이트의 전체 HTML소스 저장할 변수

        doc = Jsoup.connect(url).get(); // 사이트접속(http프로토콜로만 가능, https 프로토콜은 보안상 불가)

        Elements element = doc.select("div.sect-movie-chart"); // 전체 소스 중 일부 태그 선택

        /* Iterator을 사용하여 영화 순위 정보 가져오기 */
        Iterator<Element> movie_rank = element.select("strong.rank").iterator(); // 영화순위
        Iterator<Element> movie_name = element.select("strong.title").iterator(); // 영화이름
        Iterator<Element> movie_reserve = element.select("strong.percent span").iterator(); // 영화 예매율
        Iterator<Element> score = element.select("span.percent").iterator(); // 점수
        Iterator<Element> open_day = element.select("span.txt-info").iterator(); // 개봉일

        /* 수집된 정보 DB에 저장하기 */
        while (movie_rank.hasNext()) { // movie_rank가 다음에 있다면

            pDTO = new MovieDTO(); // DTO 만들어주고

            pDTO.setCollectTime(collectTime); // 수집시간을 PK로

            // trim() 사용 이유 : 홈페이지 개발자들이 앞뒤 공백 집어넣을수 있기 때문에 공백 제거를 함
            String rank = CmmUtil.nvl(movie_rank.next().text()).trim(); // no.1 들어옴
            pDTO.setMovieRank(rank.substring(3, rank.length())); // no.를 제거

            pDTO.setMovieNm(CmmUtil.nvl(movie_name.next().text()).trim()); // 영화 제목

            pDTO.setMovieReserve(CmmUtil.nvl(movie_reserve.next().text()).trim()); // 영화 예매율

            pDTO.setScore(CmmUtil.nvl(score.next().text()).trim()); // 영화 평점

            pDTO.setOpenDay(CmmUtil.nvl(open_day.next().text()).trim().substring(0,10)); // 영화 개봉일(앞 10자리만)

            pDTO.setRegId("admin"); // 등록자

            res += movieMapper.insertMovieInfo(pDTO); // 영화 한개씩 추가
        }

        log.info(this.getClass().getName() + ".collectMovieRank End!");

        return res;
    }

    /* 크롤링 정보 조회하기 */
    @Override
    public List<MovieDTO> getMovieInfo() throws Exception {

        log.info(this.getClass().getName() + ".getMovieInfo Start!");

        String collectTime = DateUtil.getDateTime("yyyyMMdd");

        MovieDTO pDTO =  new MovieDTO();
        pDTO.setCollectTime(collectTime);

        List<MovieDTO> rList = movieMapper.getMovieInfo(pDTO); // DB에서 조회하기

        log.info(this.getClass().getName() + ".getMovieInfo End!");

        return rList;
    }
}
