package kopo.poly.service.impl;

import kopo.poly.dto.FoodDTO;
import kopo.poly.service.IFoodService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Slf4j
public class FoodService implements IFoodService {
    @Override
    public List<FoodDTO> collectFoodInfo() throws Exception {

        log.info(this.getClass().getName() + ".collectFoodInfo Start!");

        int res = 0; // 크롤링 결과 (0보다 크면 성공)

        FoodDTO pDTO = new FoodDTO();

        List<FoodDTO> pList = new ArrayList<>();

        String url = "https://www.kopo.ac.kr/kangseo/content.do?menu=262"; // CGV 영화순위 정보 가져올 사이트 주소

        Document doc = null; // JSOUP 라이브러리를 통해 사이트에 접속되면, 그 사이트의 전체 HTML소스 저장할 변수

        doc = Jsoup.connect(url).get(); // 사이트접속(http프로토콜로만 가능, https 프로토콜은 보안상 불가)

        Elements element = doc.select("table.tbl_table menu"); // 전체 소스 중 일부 태그 선택

        /* Iterator을 사용하여 영화 순위 정보 가져오기 */
        Iterator<Element> day = element.select("td.#text").iterator(); // 영화순위
        Iterator<Element> food = element.select("td.span").iterator(); // 영화이름

        while (day.hasNext()) {

            pDTO.setDay((day.next().text()).trim());
            pDTO.setFood((food.next().text()).trim());

            pList.add(pDTO);
        }

        return pList;
    }


}
