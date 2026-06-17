package com.ysu.codereview.service;

import com.ysu.codereview.dto.PostsDto;
import com.ysu.codereview.entity.Posts;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.repository.PostSuggestRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// 변환
@Component
public class PostMapper {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    private final AccountRepository      accountRepo;
    private final PostSuggestRepository  suggestRepo;

    public PostMapper(AccountRepository accountRepo,
                      PostSuggestRepository suggestRepo) {
        this.accountRepo = accountRepo;
        this.suggestRepo = suggestRepo;
    }

    // 카드용
    public PostsDto toCard(Posts p, String meUuid) {
        PostsDto d         = new PostsDto();
        d.puid            = p.puid;
        d.title           = p.title;
        d.postType        = p.postType;
        d.themeLanguage   = p.themeLanguage;
        d.authorId        = accountRepo.findById(p.uuid).map(a -> a.getLoginId()).orElse("(알 수 없음)");
        d.createdAt       = formatDate(p.createdAt);
        d.suggestCount    = (int) suggestRepo.countByPuidAndCheakTrue(p.puid);
        d.commentCount    = 0;
        d.hasThumbnail    = false;   // 썸네일X
        d.thumbnailUrl    = null;
        d.isSuggested     = meUuid != null
                && suggestRepo.existsByPuidAndUuidAndCheakTrue(p.puid, meUuid);
        return d;
    }

    // 요약용
    public PostsDto toSummary(Posts p) {
        PostsDto d       = new PostsDto();
        d.puid          = p.puid;
        d.title         = p.title;
        d.themeLanguage = p.themeLanguage;
        d.createdAt     = formatDate(p.createdAt);
        return d;
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : date.format(DATE_FMT);
    }
}
