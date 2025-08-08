package com.xiaoyan.service;

import com.xiaoyan.vo.WeatherVO;

import java.io.IOException;

public interface WeatherService {
    WeatherVO getWeather() throws IOException, InterruptedException;

    WeatherVO refreshWeatherCache() throws IOException, InterruptedException;
}
