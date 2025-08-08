package com.xiaoyan.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaoyan.service.DeepSeekService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class DeepSeekServiceImpl implements DeepSeekService {

    @Value("${deepseek.base-url}")
    private String DEEPSEEK_API_URL;

    @Value("${deepseek.api-key}")
    private String API_KEY;

    private static final ConcurrentMap<String, List<Map<String, String>>> messages =
            new ConcurrentHashMap<>();

    private static final Map<String, String> SYSTEM_PERSONA_MESSAGE =
            Map.of("role", "system", "content", """
                    你的人设:智慧城市app的助手马嘉祺.
                    回答风格:暴躁型，像贴吧老哥.
                    我们app的具有的功能：智慧巴士、实时聊天交流、捐赠项目、个人捐赠等.
                    回答格式:纯文本""");

    private static final ObjectMapper mapper = new ObjectMapper();

    public String send(String id, String message) {
        List<Map<String, String>> currentConversation =
                messages.computeIfAbsent(id, k ->
                        Collections.synchronizedList(new ArrayList<>()));

        if (currentConversation.size() > 16) {
            currentConversation.remove(0);
            currentConversation.remove(0);
        }

        Map<String, String> newUserMessage = Map.of
                ("role", "user", "content", message);

        currentConversation.add(newUserMessage);

        List<Map<String, String>> messagesToSend = new ArrayList<>();
        messagesToSend.add(SYSTEM_PERSONA_MESSAGE);
        messagesToSend.addAll(currentConversation);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "deepseek-chat");
        requestBody.put("messages", messagesToSend);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);
            String requestBodyJson = mapper.writeValueAsString(requestBody);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> request = new HttpEntity<>(requestBodyJson, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    DEEPSEEK_API_URL,
                    request,
                    String.class
            );
            String responseBody = response.getBody();
            JsonNode root = mapper.readTree(responseBody);
            String aiContent = root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();
            Map<String, String> aiMessage =
                    Map.of("role", "assistant", "content", aiContent);
            currentConversation.add(aiMessage);

            return aiContent;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON处理失败", e);
        }
    }
}