package com.ysu.codereview.api;

import com.ysu.codereview.service.SuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
public class SuggestApiController {

    @Autowired
    private SuggestService suggestService;

    // [GET /SuggestCount?puid=1] 추천 수 조회 (AJAX용)
    @GetMapping("/SuggestCount")
    public ResponseEntity<Map<String, Long>> suggestCount(@RequestParam Long puid) {
        long count = suggestService.getSuggestCount(puid);
        log.info("추천 수 조회: puid={}, count={}", puid, count);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("count", count));
    }
}
