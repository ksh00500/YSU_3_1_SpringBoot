package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Follows;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follows, String> {

    // 팔로워
    long countByFollowingId(String followingId);

    // 팔로잉
    long countByFollowerId(String followerId);

    // 구독목록
    List<Follows> findByFollowerId(String followerId);
}
