package com.xiaoyan.task;

import com.xiaoyan.service.SmartBusService;
import com.xiaoyan.service.WeatherService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@AllArgsConstructor
public class RefreshCachesTask {

    private final SmartBusService smartBusService;
    private final WeatherService weatherService;

    @Scheduled(cron = "0 */1 * * * *")
    public void refreshCachesScheduled() {
        try {
            weatherService.refreshWeatherCache();

            smartBusService.refreshAllBusLinesCache();

        } catch (IOException e) {
            log.error("定时任务预热/刷新缓存失败: {}", e.getMessage(), e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
     @Scheduled(fixedDelay = Long.MAX_VALUE, initialDelay = 2000)
     public void initCacheOnStartup() {
         refreshCachesScheduled();
     }
}
