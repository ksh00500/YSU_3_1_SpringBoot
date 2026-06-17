package com.ysu.codereview.service;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.entity.Posts;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.repository.CommentRepository;
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
    private final CommentRepository      commentRepo;

    public PostMapper(AccountRepository accountRepo,
                      PostSuggestRepository suggestRepo,
                      CommentRepository commentRepo) {
        this.accountRepo = accountRepo;
        this.suggestRepo = suggestRepo;
        this.commentRepo = commentRepo;
    }

    // 카드용
    public PostDto toCard(Posts p, String meUuid) {
        PostDto d         = new PostDto();
        d.puid            = p.puid;
        d.title           = p.title;
        d.postType        = p.postType;
        d.themeLanguage   = p.themeLanguage;
        d.authorId        = accountRepo.findById(p.uuid).map(a -> a.id).orElse("(알 수 없음)");
        d.createdAt       = formatDate(p.createdAt);
        d.suggestCount    = (int) suggestRepo.countByPuidAndCheakTrue(p.puid);
        d.commentCount    = (int) commentRepo.countByPuid(p.puid);
        d.hasThumbnail    = false;   // 썸네일X
        d.thumbnailUrl    = null;
        d.isSuggested     = meUuid != null
                && suggestRepo.existsByPuidAndUuidAndCheakTrue(p.puid, meUuid);
        return d;
    }

    // 요약용
    public PostDto toSummary(Posts p) {
        PostDto d       = new PostDto();
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
