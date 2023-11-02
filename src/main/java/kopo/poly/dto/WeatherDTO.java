package kopo.poly.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class WeatherDTO {
    private String weather;
    private String degree;
    private String lat;
    private String lon;
    private double currentTemp;
    private List<WeatherDailyDTO> dailyList;
}
