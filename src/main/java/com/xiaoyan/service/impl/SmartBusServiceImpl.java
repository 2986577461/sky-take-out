package com.xiaoyan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyan.service.SmartBusService;
import com.xiaoyan.vo.SmartBusVO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SmartBusServiceImpl implements SmartBusService {

    @Value("${smart-city.bus-api}")
    private String SMART_BUS_API;

    @Value("${smart-city.site}")
    private List<String> site;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Cacheable(cacheNames = "bus", key = "'allBusLines'")
    public List<SmartBusVO> getAll() throws JsonProcessingException {
        return fetchBusDataFromApi();
    }

    @CachePut(cacheNames = "bus", key = "'allBusLines'")
    public List<SmartBusVO> refreshAllBusLinesCache() throws JsonProcessingException {
        return fetchBusDataFromApi();
    }

    private List<SmartBusVO> fetchBusDataFromApi() throws JsonProcessingException {
        List<SmartBusVO> list = new ArrayList<>();
        for (String s : site) {
            ResponseEntity<String> resp = restTemplate.getForEntity(
                    SMART_BUS_API + "?type=json&city=绵阳&line=" + s, String.class);
            SmartBusVO smartBusVO = new SmartBusVO();
            JsonNode rootNode = objectMapper.readTree(resp.getBody());
            smartBusVO.setDestination(rootNode.path("line").asText());
            smartBusVO.setCar_count(rootNode.path("car_count").asInt());
            List<SmartBusVO.LineItem> lineItems = new ArrayList<>();

            JsonNode dataNode = rootNode.path("data");
            for (JsonNode jsonNode : dataNode) {
                SmartBusVO.LineItem lineItem = new SmartBusVO.LineItem();
                lineItem.setLineName(jsonNode.path("lines").asText());
                lineItem.setEndDestination(jsonNode.path("endSn").asText());

                String reachtime = jsonNode.path("reachtime").asText();
                lineItem.setReachTime(LocalDateTime.parse(reachtime, FORMATTER));

                lineItem.setRemainingTime(jsonNode.path("travelTime").asText());
                lineItem.setSurplus(jsonNode.path("surplus").asText());
                lineItem.setBusId(jsonNode.path("busId").asText());
                lineItems.add(lineItem);
            }
            smartBusVO.setLineItems(lineItems);
            list.add(smartBusVO);
        }
        return list;
    }















    // 为 CompletableFuture 创建一个专用的线程池
    // 线程池大小可以根据你的服务器核心数和外部API的并发限制进行调整
    // 例如，可以使用固定线程池，或者基于CPU核心数来决定
//    private final ExecutorService executorService = Executors.newFixedThreadPool(8);
    // 假设最多10个并发请求，或site大小

//
//    private List<SmartBusVO> fetchBusDataFromApi() {
//        log.info("开始并发从外部API获取公交数据，共 {} 条线路", site.size());
//
//        // 使用 Stream API 结合 CompletableFuture 实现并发
//        List<CompletableFuture<SmartBusVO>> futures = site.stream()
//                .map(line -> CompletableFuture.supplyAsync(() -> {
//                    // 每个 CompletableFuture 代表一个独立的API调用任务
//                    SmartBusVO smartBusVO = new SmartBusVO();
//                    try {
//                        String url = SMART_BUS_API + "?type=json&city=绵阳&line=" + line;
//                        log.debug("正在请求公交线路: {}", url);
//                        ResponseEntity<String> resp = restTemplate.getForEntity(url, String.class);
//                        String responseBody = resp.getBody();
//
//                        // 解析JSON
//                        JsonNode rootNode = objectMapper.readTree(responseBody);
//                        smartBusVO.setDestination(rootNode.path("line").asText());
//                        smartBusVO.setCar_count(rootNode.path("car_count").asInt());
//                        List<SmartBusVO.LineItem> lineItems = new ArrayList<>();
//
//                        JsonNode dataNode = rootNode.path("data");
//
//                        for (JsonNode jsonNode : dataNode) {
//                            SmartBusVO.LineItem lineItem = SmartBusVO.LineItem.builder() // 使用 builder 模式更方便
//                                    .lineName(jsonNode.path("lines").asText())
//                                    .endDestination(jsonNode.path("endSn").asText())
//                                    .reachTime(LocalDateTime.parse(jsonNode.path("reachtime").asText(), FORMATTER))
//                                    .remainingTime(jsonNode.path("travelTime").asText())
//                                    .surplus(jsonNode.path("surplus").asText())
//                                    .busId(jsonNode.path("busId").asText())
//                                    .build();
//
//                            lineItems.add(lineItem);
//                        }
//                        smartBusVO.setLineItems(lineItems);
//                    } catch (Exception e) {
//                        log.error("获取公交线路 {} 数据失败: {}", line, e.getMessage(), e);
//                        // 返回一个空的 SmartBusVO 或带有错误信息的，以便后续聚合
//                        smartBusVO.setDestination(line + " (获取失败)");
//                    }
//                    System.out.println(smartBusVO);
//                    return smartBusVO;
//                }, executorService)) // 指定线程池
//                .toList(); // 将所有的 CompletableFuture 收集起来
//
//        // 等待所有异步任务完成，并收集结果
//        // allOf 等待所有 CompletableFuture 完成，thenApplyAsync 收集结果
//        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//
//        List<SmartBusVO> allBusData = allFutures.thenApply(v ->
//                futures.stream()
//                        .map(CompletableFuture::join) // join() 获取结果，如果任务异常会抛出
//                        .collect(Collectors.toList())
//        ).join(); // join() 等待并获取最终结果
//
//        log.info("所有公交数据并发获取完成，共 {} 条线路结果", allBusData.size());
//        return allBusData;
//    }
//
//    // 在应用程序关闭时优雅地关闭线程池，防止资源泄露
//    @PreDestroy
//    public void shutdown() {
//        executorService.shutdown();
//        log.info("SmartBusService 线程池已关闭。");
//    }

}
