package kopo.poly.service.impl;

import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.CmmUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Slf4j
@Service
public class WeatherService implements IWeatherService {
    @Override
    public WeatherDTO getWeatherInfo() throws Exception {

        log.info(this.getClass().getName() + ".getWeatherInfo Start!");

        int res = 0; // 크롤링 결과 (0보다 크면 성공)

        String url = "https://weather.naver.com/"; // 날씨 정보 가져올 사이트 주소

        Document doc = null; // JSOUP 라이브러리를 통해 사이트에 접속되면, 그 사이트의 전체 HTML소스 저장할 변수

        doc = Jsoup.connect(url).get(); // 사이트접속(http프로토콜로만 가능, https 프로토콜은 보안상 불가)

        Elements element = doc.select("div.section_center"); // 전체 소스 중 일부 태그 선택

        /* Iterator을 사용하여 식단 정보 가져오기 */
        Iterator<Element> degree = element.select("strong.current").iterator();
        Iterator<Element> weather = element.select("span.weather").iterator();

        WeatherDTO pDTO = new WeatherDTO();

        while (degree.hasNext() && weather.hasNext()) {
            pDTO.setWeather(weather.next().text());
            pDTO.setDegree(degree.next().text());
        }

        log.info("Weather : " + pDTO.getWeather());
        log.info("Degree : " + pDTO.getDegree());

        log.info(this.getClass().getName() + ".getWeatherInfo End!");

        return pDTO;
    }
}
