package kopo.poly.controller;

import kopo.poly.dto.FoodDTO;
import kopo.poly.dto.WeatherDTO;
import kopo.poly.service.IWeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/weather")
@Controller
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
}
