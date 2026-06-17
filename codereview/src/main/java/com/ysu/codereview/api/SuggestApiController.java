// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.api;

import com.ysu.codereview.service.SuggestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// 추천 기능 REST API Controller
// @RestController = JSON 데이터를 반환하는 API 컨트롤러
@Slf4j
@RestController
public class SuggestApiController {

    @Autowired
    private SuggestService suggestService; // 추천 비즈니스 로직을 Service에 위임

    // [POST /Suggest?postId=1&nickname=홍길동] 추천 등록
    // 이미 추천한 경우 "이미 추천한 게시글입니다." 메시지 반환
    @PostMapping("/Suggest")
    public ResponseEntity<String> suggest(@RequestParam Long postId,
                                          @RequestParam String nickname) {
        log.info("추천 요청: postId={}, nickname={}", postId, nickname);
        String result = suggestService.addSuggest(postId, nickname);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // [POST /CancelSuggest?postId=1&nickname=홍길동] 추천 취소
    @PostMapping("/CancelSuggest")
    public ResponseEntity<String> cancelSuggest(@RequestParam Long postId,
                                                @RequestParam String nickname) {
        log.info("추천 취소 요청: postId={}, nickname={}", postId, nickname);
        String result = suggestService.cancelSuggest(postId, nickname);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // [GET /SuggestCount?postId=1] 추천 수 조회
    // 반환 예시: {"count": 5}
    @GetMapping("/SuggestCount")
    public ResponseEntity<Map<String, Long>> suggestCount(@RequestParam Long postId) {
        long count = suggestService.getSuggestCount(postId);
        log.info("추천 수 조회: postId={}, count={}", postId, count);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("count", count));
    }
}
