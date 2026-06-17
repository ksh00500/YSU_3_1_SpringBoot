package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follows, String> {

    /** 팔로워 수 — 나를 구독한 사람 수 */
    long countByFollowingId(String followingId);

    /** 팔로잉 수 — 내가 구독한 사람 수 */
    long countByFollowerId(String followerId);

    /** 내가 구독한 관계 목록 (followingId 추출용) */
    List<Follows> findByFollowerId(String followerId);
}
