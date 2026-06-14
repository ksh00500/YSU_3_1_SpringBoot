// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.repository;

import com.example.community.entity.Suggest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Suggest 테이블에 접근하는 Repository
public interface SuggestRepository extends JpaRepository<Suggest, Long> {

    // 특정 게시글의 추천 수 조회
    long countByPostId(Long postId);

    // 특정 게시글에 특정 닉네임이 추천했는지 조회 (추천 취소할 때 사용)
    Optional<Suggest> findByPostIdAndNickname(Long postId, String nickname);

    // 특정 게시글에 특정 닉네임이 이미 추천했는지 여부 확인 (중복 추천 방지에 사용)
    boolean existsByPostIdAndNickname(Long postId, String nickname);
}
