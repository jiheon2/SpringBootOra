package kopo.poly.service.impl;

import kopo.poly.dto.FoodDTO;
import kopo.poly.service.IFoodService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
public class FoodService implements IFoodService {

    @Scheduled(cron = "2 * * * * *")
    @Override
    public List<FoodDTO> toDayFood() throws Exception {

        log.info(this.getClass().getName() + ".toDayFood Start!");

        int res = 0; // 크롤링 결과 (0보다 크면 성공)

        String url = "https://www.kopo.ac.kr/kangseo/content.do?menu=262"; // 식단 정보 가져올 사이트 주소

        Document doc = null; // JSOUP 라이브러리를 통해 사이트에 접속되면, 그 사이트의 전체 HTML소스 저장할 변수

        doc = Jsoup.connect(url).get(); // 사이트접속(http프로토콜로만 가능, https 프로토콜은 보안상 불가)

        Elements element = doc.select("table.tbl_table tbody"); // 전체 소스 중 일부 태그 선택

        /* Iterator을 사용하여 식단 정보 가져오기 */
        Iterator<Element> foodIt = element.select("tr").iterator(); // 식단정보

        FoodDTO pDTO = null;

        List<FoodDTO> pList = new ArrayList<>();
        int idx = 0; // 반복횟수를 월부터 금까지(5일만)

        while (foodIt.hasNext()) {

            /* 반복횟수 카운팅
            5번째가 금요일이라 6번째 토요일은 실행안되게 하기 위함
            반복문 5번만 돌기(월요일부터 금요일까지만 넣기)
             */
            if (idx++ > 4) {

                break;

            }

            pDTO = new FoodDTO();

            String food = CmmUtil.nvl(foodIt.next().text()).trim(); // 요일별 식단정보 들어옴

            log.info("food : " + food);

            pDTO.setDay(food.substring(0, 3)); // 앞 3글자 요일 저장

            pDTO.setFood(food.substring(4)); // 식단 정보

            pList.add(pDTO);

        }

        log.info(this.getClass().getName() + ".toDayFood End!");

        return pList;
    }


}
