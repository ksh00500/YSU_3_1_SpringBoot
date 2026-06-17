package com.ysu.codereview.service;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.dto.UserDto;
import com.ysu.codereview.entity.Posts;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.repository.FollowRepository;
import com.ysu.codereview.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/** 개인 피드 — 프로필/내 글/삭제 */
@Service
public class FeedService {

    private final AccountRepository accountRepo;
    private final PostRepository    postRepo;
    private final FollowRepository  followRepo;
    private final PostMapper        mapper;

    public FeedService(AccountRepository accountRepo,
                       PostRepository postRepo,
                       FollowRepository followRepo,
                       PostMapper mapper) {
        this.accountRepo = accountRepo;
        this.postRepo = postRepo;
        this.followRepo = followRepo;
        this.mapper = mapper;
    }

    /** 프로필 카드 정보 */
    public UserDto user(String meUuid) {
        UserDto u = new UserDto();
        u.id             = accountRepo.findById(meUuid).map(a -> a.id).orElse("(알 수 없음)");
        u.followerCount  = (int) followRepo.countByFollowingId(meUuid);
        u.followingCount = (int) followRepo.countByFollowerId(meUuid);
        return u;
    }

    /** 내 글 목록 (요약 필드) */
    public List<PostDto> myPosts(String meUuid) {
        return postRepo.findByUuidOrderByCreatedAtDesc(meUuid).stream()
                .map(mapper::toSummary)
                .collect(Collectors.toList());
    }

    /** 내 글 삭제 — 본인 소유 글만 삭제 */
    @Transactional
    public void deletePost(String meUuid, String puid) {
        if (puid == null || puid.isEmpty()) {
            return;
        }
        postRepo.findById(puid)
                .filter(p -> meUuid.equals(p.uuid))
                .ifPresent(postRepo::delete);
    }
}
