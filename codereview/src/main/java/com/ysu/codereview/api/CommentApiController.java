package com.ysu.codereview.api;

import com.ysu.codereview.dto.CommentDto;
import com.ysu.codereview.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class CommentApiController {

    @Autowired
    private CommentService commentService;

    // [GET /CommentList?puid=1] 댓글 목록 (AJAX용)
    @GetMapping("/CommentList")
    public ResponseEntity<List<CommentDto>> commentList(@RequestParam Long puid) {
        log.info("댓글 목록 조회: puid={}", puid);
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getComments(puid, null));
    }
}
