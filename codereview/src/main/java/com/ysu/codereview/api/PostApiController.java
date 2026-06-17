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

@Slf4j
@RestController
public class PostApiController {

    @Autowired
    private PostService postService;

    // [GET /RelatedPostList?puid=1] 관련 게시글 목록 (AJAX용)
    @GetMapping("/RelatedPostList")
    public ResponseEntity<List<PostDto>> relatedPostList(@RequestParam Long puid) {
        log.info("관련 게시글 조회: puid={}", puid);
        return ResponseEntity.status(HttpStatus.OK).body(postService.getRelatedPosts(puid));
    }

}
