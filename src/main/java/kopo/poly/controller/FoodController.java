//package kopo.poly.controller;
//
//import kopo.poly.dto.FoodDTO;
//import kopo.poly.dto.MovieDTO;
//import kopo.poly.service.impl.FoodService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@RequiredArgsConstructor
//@RequestMapping(value = "/food")
//@Controller
//public class FoodController {
//    @GetMapping(value = "/todayFood")
//    public String getFoodInfo(HttpServletRequest request, ModelMap model) throws Exception {
//
//        log.info(this.getClass().getName() + ".getFoodInfo Start!");
//
//        List<FoodDTO> rList = Optional.ofNullable(FoodService.getMovieInfo()).orElseGet(ArrayList::new); // 수집된 영화 정보 조회
//
//        model.addAttribute("rList", rList); // 조회 결과 jsp로 전달
//
//        log.info(this.getClass().getName() + ".getMovieRank End!");
//
//        return "/movie/movieList";
//    }
//
//}
