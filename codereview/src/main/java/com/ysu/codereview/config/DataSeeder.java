package com.ysu.codereview.config;

import com.ysu.codereview.entity.Account;
import com.ysu.codereview.entity.Comment;
import com.ysu.codereview.entity.Follows;
import com.ysu.codereview.entity.PostSuggests;
import com.ysu.codereview.entity.Posts;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.repository.CommentRepository;
import com.ysu.codereview.repository.FollowRepository;
import com.ysu.codereview.repository.PostRepository;
import com.ysu.codereview.repository.PostSuggestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * 개발용 시드 데이터. H2 인메모리라 기동 시마다 초기화됨.
 * "me"(현재 로그인 사용자) 기본값은 u-testuser.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final AccountRepository     accountRepo;
    private final PostRepository        postRepo;
    private final PostSuggestRepository suggestRepo;
    private final CommentRepository     commentRepo;
    private final FollowRepository      followRepo;

    public DataSeeder(AccountRepository accountRepo, PostRepository postRepo,
                      PostSuggestRepository suggestRepo, CommentRepository commentRepo,
                      FollowRepository followRepo) {
        this.accountRepo = accountRepo;
        this.postRepo = postRepo;
        this.suggestRepo = suggestRepo;
        this.commentRepo = commentRepo;
        this.followRepo = followRepo;
    }

    @Override
    public void run(String... args) {
        if (accountRepo.count() > 0) return;

        // ── 사용자 ──
        accountRepo.saveAll(List.of(
                new Account("u-testuser",  "testuser",  "1234", false),
                new Account("u-devholic",  "devholic",  "1234", false),
                new Account("u-coder99",   "coder99",   "1234", false),
                new Account("u-rustlover", "rustlover", "1234", false),
                new Account("u-jobhunter", "jobhunter", "1234", false),
                new Account("u-gopher",    "gopher",    "1234", false),
                new Account("u-seniordev", "seniordev", "1234", false)
        ));

        // ── 게시글 (post-1·2는 testuser 작성 → 피드용) ──
        postRepo.saveAll(List.of(
                post("post-1", "u-testuser",  "tech",   "Python",     "Python으로 만드는 간단한 AI 챗봇", LocalDate.of(2025, 6, 4)),
                post("post-2", "u-testuser",  "tech",   "JavaScript", "React 성능 최적화 실전 가이드",     LocalDate.of(2025, 5, 28)),
                post("post-3", "u-rustlover", "tech",   "Rust",       "Rust로 웹서버 구축하기 — 실전편",   LocalDate.of(2025, 6, 1)),
                post("post-4", "u-jobhunter", "career", "Java",       "취업 준비생을 위한 포트폴리오 전략", LocalDate.of(2025, 5, 30)),
                post("post-5", "u-gopher",    "tech",   "Go",         "Go 고루틴 완전 이해하기",           LocalDate.of(2025, 6, 2)),
                post("post-6", "u-seniordev", "career", "JavaScript", "코드 리뷰 잘 받는 7가지 방법",      LocalDate.of(2025, 5, 25))
        ));

        // ── 추천 (puid → 추천 수). me(u-testuser)는 post-3·post-5 추천 ──
        seedSuggests("post-1", 5, false);
        seedSuggests("post-2", 3, false);
        seedSuggests("post-3", 8, true);
        seedSuggests("post-4", 2, false);
        seedSuggests("post-5", 6, true);
        seedSuggests("post-6", 4, false);

        // ── 댓글 (puid → 댓글 수) ──
        seedComments("post-1", 3);
        seedComments("post-2", 1);
        seedComments("post-3", 2);
        seedComments("post-5", 2);
        seedComments("post-6", 1);

        // ── 구독 ── me가 rustlover·gopher 구독(→구독탭 post-3·5), 3명이 me 구독
        followRepo.saveAll(List.of(
                new Follows("f-1", "u-testuser", "u-rustlover"),
                new Follows("f-2", "u-testuser", "u-gopher"),
                new Follows("f-3", "u-devholic", "u-testuser"),
                new Follows("f-4", "u-coder99",  "u-testuser"),
                new Follows("f-5", "u-seniordev","u-testuser")
        ));
    }

    private Posts post(String puid, String uuid, String type, String lang, String title, LocalDate date) {
        return new Posts(puid, uuid, type, title,
                "<p>" + title + " 본문입니다.</p>", lang, date);
    }

    private int suggestSeq = 0;
    private void seedSuggests(String puid, int count, boolean includeMe) {
        String[] voters = {"u-devholic", "u-coder99", "u-rustlover", "u-jobhunter", "u-gopher", "u-seniordev"};
        for (int i = 0; i < count; i++) {
            String voter = includeMe && i == 0 ? "u-testuser" : voters[i % voters.length];
            suggestRepo.save(new PostSuggests("s-" + (++suggestSeq), puid, voter, true));
        }
    }

    private int commentSeq = 0;
    private void seedComments(String puid, int count) {
        String[] writers = {"u-devholic", "u-coder99", "u-testuser", "u-gopher"};
        for (int i = 0; i < count; i++) {
            commentRepo.save(new Comment("c-" + (++commentSeq), puid,
                    writers[i % writers.length], "샘플 댓글 " + (i + 1)));
        }
    }
}
