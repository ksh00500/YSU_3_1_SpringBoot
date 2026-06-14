// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.api;

import com.example.community.dto.CommentDto;
import com.example.community.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 댓글 기능 REST API Controller
// @RestController = JSON 데이터를 반환하는 API 컨트롤러
@Slf4j
@RestController
public class CommentApiController {

    @Autowired
    private CommentService commentService; // 댓글 비즈니스 로직을 Service에 위임

    // [GET /CommentList?postId=1] 특정 게시글의 댓글 목록 조회
    // 반환: 댓글 목록 JSON 배열
    @GetMapping("/CommentList")
    public ResponseEntity<List<CommentDto>> commentList(@RequestParam Long postId) {
        log.info("댓글 목록 조회: postId={}", postId);
        List<CommentDto> comments = commentService.getComments(postId);
        return ResponseEntity.status(HttpStatus.OK).body(comments);
    }

    // [POST /CommentAdd?postId=1&nickname=홍길동&body=좋은코드네요] 댓글 등록
    // 반환: 등록된 댓글 JSON (HTTP 201 Created)
    @PostMapping("/CommentAdd")
    public ResponseEntity<CommentDto> commentAdd(@RequestParam Long postId,
                                                 @RequestParam String nickname,
                                                 @RequestParam String body) {
        log.info("댓글 등록: postId={}, nickname={}", postId, nickname);
        CommentDto created = commentService.addComment(postId, nickname, body);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // [POST /CommentEdit?commentId=1&body=수정된내용] 댓글 수정
    // 반환: 수정된 댓글 JSON
    @PostMapping("/CommentEdit")
    public ResponseEntity<CommentDto> commentEdit(@RequestParam Long commentId,
                                                  @RequestParam String body) {
        log.info("댓글 수정: commentId={}", commentId);
        CommentDto updated = commentService.editComment(commentId, body);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }

    // [POST /CommentDel?commentId=1] 댓글 삭제
    // 반환: 삭제된 댓글 JSON
    @PostMapping("/CommentDel")
    public ResponseEntity<CommentDto> commentDel(@RequestParam Long commentId) {
        log.info("댓글 삭제: commentId={}", commentId);
        CommentDto deleted = commentService.deleteComment(commentId);
        return ResponseEntity.status(HttpStatus.OK).body(deleted);
    }
}
