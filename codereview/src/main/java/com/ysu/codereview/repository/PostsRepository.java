package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Posts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, String> {

    List<Posts> findAllByOrderByCreatedAtDesc();

    List<Posts> findByUuidInOrderByCreatedAtDesc(List<String> uuids);

    List<Posts> findByUuidOrderByCreatedAtDesc(String uuid);

    List<Posts> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);
}
