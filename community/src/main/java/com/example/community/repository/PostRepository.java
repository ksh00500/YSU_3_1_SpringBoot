// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.repository;

import com.example.community.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Post 테이블에 접근하는 Repository (JpaRepository를 상속하면 기본 CRUD 메서드 자동 제공)
public interface PostRepository extends JpaRepository<Post, Long> {

    // 같은 언어(language)를 가진 게시글 중 특정 id를 제외하고 조회 (관련 게시글 기능에 사용)
    List<Post> findByLanguageAndIdNot(String language, Long excludeId);
}
