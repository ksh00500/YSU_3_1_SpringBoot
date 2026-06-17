package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Posts, String> {

    /** 최신글 탭 */
    List<Posts> findAllByOrderByCreatedAtDesc();

    /** 구독한 글 탭 — 내가 구독한 작성자들의 글 */
    List<Posts> findByUuidInOrderByCreatedAtDesc(List<String> uuids);

    /** 개인 피드 — 내 글 목록 */
    List<Posts> findByUuidOrderByCreatedAtDesc(String uuid);

    /** 검색 */
    List<Posts> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
}
