package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, String> {

    // 댓글 수
    long countByPuid(String puid);
}
