// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Comment 테이블에 접근하는 Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글(postId)에 달린 댓글 목록을 전부 조회
    List<Comment> findByPostId(Long postId);
}
