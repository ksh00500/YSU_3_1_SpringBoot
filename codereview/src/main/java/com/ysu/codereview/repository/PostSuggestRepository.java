package com.ysu.codereview.repository;

import com.ysu.codereview.entity.PostSuggests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostSuggestRepository extends JpaRepository<PostSuggests, String> {

    /** 추천 수 */
    long countByPuidAndCheakTrue(String puid);

    /** 현재 사용자의 추천 여부 */
    boolean existsByPuidAndUuidAndCheakTrue(String puid, String uuid);
}
