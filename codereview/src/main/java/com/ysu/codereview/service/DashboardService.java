package com.ysu.codereview.service;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.entity.Follows;
import com.ysu.codereview.entity.Posts;
import com.ysu.codereview.repository.FollowRepository;
import com.ysu.codereview.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 대시보드
@Service
public class DashboardService {

    private final PostRepository   postRepo;
    private final FollowRepository followRepo;
    private final PostMapper       mapper;

    public DashboardService(PostRepository postRepo,
                            FollowRepository followRepo,
                            PostMapper mapper) {
        this.postRepo = postRepo;
        this.followRepo = followRepo;
        this.mapper = mapper;
    }

    // 인기글
    public List<PostDto> trendy(String meUuid) {
        return toCards(postRepo.findAllByOrderByCreatedAtDesc(), meUuid).stream()
                .sorted(Comparator.comparingInt((PostDto d) -> d.suggestCount).reversed())
                .collect(Collectors.toList());
    }

    // 최신글
    public List<PostDto> latest(String meUuid) {
        return toCards(postRepo.findAllByOrderByCreatedAtDesc(), meUuid);
    }

    // 구독글
    public List<PostDto> follow(String meUuid) {
        List<String> followingIds = followRepo.findByFollowerId(meUuid).stream()
                .map(f -> f.followingId)
                .collect(Collectors.toList());
        if (followingIds.isEmpty()) {
            return List.of();
        }
        return toCards(postRepo.findByUuidInOrderByCreatedAtDesc(followingIds), meUuid);
    }

    // 언어별
    public List<PostDto> theme(String meUuid) {
        return toCards(postRepo.findAllByOrderByCreatedAtDesc(), meUuid).stream()
                .sorted(Comparator.comparing((PostDto d) -> d.themeLanguage == null ? "" : d.themeLanguage)
                        .thenComparing(Comparator.comparingInt((PostDto d) -> d.suggestCount).reversed()))
                .collect(Collectors.toList());
    }

    // 검색
    public List<PostDto> search(String keyword, String meUuid) {
        String kw = keyword == null ? "" : keyword.trim();
        return toCards(postRepo.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(kw), meUuid);
    }

    private List<PostDto> toCards(List<Posts> posts, String meUuid) {
        return posts.stream()
                .map(p -> mapper.toCard(p, meUuid))
                .collect(Collectors.toList());
    }
}
