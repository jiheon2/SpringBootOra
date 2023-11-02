package kopo.poly.controller;

import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IWeatherService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/weather")
@Controller // @ResponseBody을 많이 사용해야할 경우 컨트롤러 자체를 @RestController로 선언해라
public class WeatherController {
    private final IWeatherService weatherService;

    @GetMapping(value = "todayWeather")
    public String getWeatherInfo(ModelMap model, HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".getWeatherInfo Start!");

        WeatherDTO rDTO = weatherService.getWeatherInfo();

        model.addAttribute("rDTO", rDTO); // 조회 결과 jsp로 전달

        log.info(this.getClass().getName() + ".getWeatherInfo End!");

        return "/weather/todayWeather";
    }

    @GetMapping(value = "getWeather")
    @ResponseBody
    public WeatherDTO getWeather(HttpServletRequest request) throws Exception {
        log.info(this.getClass().getName() + ".getWeather Start!");

        String lat = CmmUtil.nvl(request.getParameter("lat"));
        String lon = CmmUtil.nvl(request.getParameter("lon"));

        WeatherDTO pDTO = new WeatherDTO();
        pDTO.setLat(lat);
        pDTO.setLon(lon);

        log.info("위도 : " + lat);
        log.info("경도 : " + lon);

        WeatherDTO rDTO = Optional.ofNullable(weatherService.getWeather(pDTO)).orElseGet(WeatherDTO::new);
//
//        if (rDTO == null) {
//            rDTO = new WeatherDTO();
//        }

        log.info(this.getClass().getName() + ".getWeather End!");

        return rDTO;
    }
}
