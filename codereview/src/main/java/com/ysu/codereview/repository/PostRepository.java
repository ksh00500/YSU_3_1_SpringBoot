package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, String> {

    // 최신글
    List<Posts> findAllByOrderByCreatedAtDesc();

    // 구독글
    List<Posts> findByUuidInOrderByCreatedAtDesc(List<String> uuids);

    // 내 글
    List<Posts> findByUuidOrderByCreatedAtDesc(String uuid);

    // 검색
    List<Posts> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
}
