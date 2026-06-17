package com.ysu.codereview.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

@Service
public class AiService {

    @Value("${grok.api.key}")
    private String apiKey;

    @Value("${grok.api.url}")
    private String apiUrl;

    @Value("${grok.model}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String refactorCode(String code, String language) {
        try {
            String prompt = "다음 " + language + " 코드를 분석하고 리팩토링해주세요.\n\n"
                    + "아래 형식으로 한국어로 응답해주세요:\n\n"
                    + "## 코드 분석\n"
                    + "(코드의 문제점, 개선 포인트를 3~5줄로 설명)\n\n"
                    + "## 리팩토링 결과\n"
                    + "(개선된 전체 코드)\n\n"
                    + "코드:\n```\n" + code + "\n```";

            String requestBody = objectMapper.writeValueAsString(Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of("role", "user", "content", prompt)
                    ),
                    "max_tokens", 4096
            ));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();

            JsonNode root = objectMapper.readTree(body);

            // 에러 응답 처리
            if (root.has("error")) {
                return "API 오류 (원시):\n" + body;
            }

            JsonNode choices = root.path("choices");
            if (choices.isEmpty()) {
                return "응답 없음. 원시 응답:\n" + body;
            }

            return choices.get(0).path("message").path("content").asText();

        } catch (Exception e) {
            return "AI 분석 중 오류가 발생했습니다.\n오류 내용: " + e.getMessage();
        }
    }
}
