// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.api;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

// 게시글 관련 REST API Controller (관련 게시글, AI 리팩토링)
// @RestController = JSON 데이터를 반환하는 API 컨트롤러
@Slf4j
@RestController
public class PostApiController {

    @Autowired
    private PostService postService; // 게시글 비즈니스 로직을 Service에 위임

    // [GET /RelatedPostList?postId=1] 관련 게시글 목록 조회
    // 현재 게시글과 같은 프로그래밍 언어의 게시글을 최대 5개 반환
    @GetMapping("/RelatedPostList")
    public ResponseEntity<List<PostDto>> relatedPostList(@RequestParam Long postId) {
        log.info("관련 게시글 조회: postId={}", postId);
        List<PostDto> related = postService.getRelatedPosts(postId);
        return ResponseEntity.status(HttpStatus.OK).body(related);
    }

    // [GET /AicodeEdit?postId=1] AI 코드 리팩토링 요청
    // 반환 예시: {"result": "[AI 리팩토링 결과]\n언어: Java\n\npublic class ..."}
    @GetMapping("/AicodeEdit")
    public ResponseEntity<Map<String, String>> aiCodeEdit(@RequestParam Long postId) {
        log.info("AI 리팩토링 요청: postId={}", postId);
        String result = postService.getAiRefactored(postId);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result", result));
    }
}
