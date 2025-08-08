package com.xiaoyan.controller;



import com.xiaoyan.result.Result;
import com.xiaoyan.service.WeatherService;
import com.xiaoyan.vo.WeatherVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@Slf4j
@RequestMapping("weather")
@Tag(name = "天气功能")
@AllArgsConstructor
public class WeatherController {

    private WeatherService weatherService;

    @GetMapping
    @Operation(summary = "获取天气")
    public Result<WeatherVO> getWeather() throws IOException, InterruptedException {
        WeatherVO weather = weatherService.getWeather();
        return Result.success(weather);
    }
}
