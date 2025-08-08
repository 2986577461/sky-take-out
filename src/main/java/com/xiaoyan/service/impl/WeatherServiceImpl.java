package com.xiaoyan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyan.service.WeatherService;
import com.xiaoyan.vo.WeatherVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Value("${smart-city.weather.city}")
    private String city;

    @Value("${smart-city.weather.province}")
    private String province;

    @Value("${smart-city.weather.county}")
    private String county;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(cacheNames = "weather", key = "'currentWeather'")
    public WeatherVO getWeather() throws IOException, InterruptedException {
        return fetchWeatherFromApi();
    }

    @CachePut(cacheNames = "weather", key = "'currentWeather'")
    public WeatherVO refreshWeatherCache() throws IOException, InterruptedException {
        return fetchWeatherFromApi();
    }

    private WeatherVO fetchWeatherFromApi() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://wis.qq.com/weather/" +
                        "common?source=pc&weather_type=observe&" +
                        "province=" + province + "&city=" + city + "&county=" + county))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        try {
                        JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode observeNode = rootNode.path("data").path("observe");

            return WeatherVO.builder().
                    windPower(observeNode.path("wind_power").asText()).
                    windDirectionName(observeNode.path("wind_direction_name").asText()).
                    weather(observeNode.path("weather").asText()).
                    degree(observeNode.path("degree").asText()).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
