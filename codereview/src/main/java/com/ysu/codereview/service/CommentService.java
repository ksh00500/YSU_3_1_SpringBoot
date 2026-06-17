// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.service;

import com.ysu.codereview.dto.CommentDto;
import com.ysu.codereview.entity.Comment;
import com.ysu.codereview.entity.Post;
import com.ysu.codereview.repository.CommentRepository;
import com.ysu.codereview.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// 댓글 관련 비즈니스 로직을 처리하는 Service
@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository; // 댓글 DB 접근

    @Autowired
    private PostRepository postRepository; // 댓글이 달릴 게시글 확인용

    // [GET /CommentList] 특정 게시글의 댓글 목록 조회
    public List<CommentDto> getComments(Long postId, String currentLoginId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(c -> CommentDto.of(c, currentLoginId != null && currentLoginId.equals(c.getNickname())))
                .collect(Collectors.toList());
    }

    // [POST /CommentAdd] 댓글 등록
    // 게시글이 존재하는지 먼저 확인 후 댓글 저장
    @Transactional
    public CommentDto addComment(Long postId, String nickname, String body) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        Comment comment = new Comment(null, post, nickname, body, null);
        Comment saved = commentRepository.save(comment);
        log.info("댓글 등록 완료: {}", saved);
        return CommentDto.of(saved);
    }

    // [POST /CommentEdit] 댓글 수정
    // 댓글 id로 찾아서 내용(body)만 변경
    @Transactional
    public CommentDto editComment(Long commentId, String body) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id=" + commentId));

        comment.updateBody(body); // Entity의 수정 메서드 호출
        Comment updated = commentRepository.save(comment);
        log.info("댓글 수정 완료: {}", updated);
        return CommentDto.of(updated);
    }

    // [POST /CommentDel] 댓글 삭제
    // 댓글 id로 찾아서 삭제 후 삭제된 댓글 정보 반환
    @Transactional
    public CommentDto deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다. id=" + commentId));

        commentRepository.delete(comment);
        log.info("댓글 삭제 완료: commentId={}", commentId);
        return CommentDto.of(comment);
    }
}
