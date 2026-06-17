package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByLanguageAndIdNot(String language, Long excludeId);

    List<Post> findByPostTypeOrderByCreatedAtDesc(String postType);

    List<Post> findByLanguageOrderByCreatedAtDesc(String language);

    List<Post> findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(String keyword);

    List<Post> findByNicknameOrderByCreatedAtDesc(String nickname);
}
