package com.ysu.codereview.controller;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.service.DashboardService;
import com.ysu.codereview.service.FeedService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/** 대시보드(메인) + 개인 피드 담당. 그 외 페이지는 별도 컨트롤러. */
@Controller
public class DevlogController {

    /** 인증 미구현 — 세션에 loginUuid 없으면 데모 사용자로 폴백 */
    private static final String DEMO_USER = "u-testuser";

    private final DashboardService dashboardService;
    private final FeedService      feedService;
    private final AccountRepository accountRepo;

    public DevlogController(DashboardService dashboardService,
                            FeedService feedService,
                            AccountRepository accountRepo) {
        this.dashboardService = dashboardService;
        this.feedService = feedService;
        this.accountRepo = accountRepo;
    }

    private String me(HttpSession session) {
        Object u = session.getAttribute("loginUuid");
        return u != null ? u.toString() : DEMO_USER;
    }

    private String usernameOf(String uuid) {
        return accountRepo.findById(uuid).map(a -> a.id).orElse("guest");
    }

    // ═══════════════════════════════════════════════════════════════
    //  메인 페이지 (대시보드) — 탭별 게시글
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/")
    public String main(Model model, HttpSession session) {
        return renderMain("trendy", "DevLog", dashboardService.trendy(me(session)), model, session);
    }

    @GetMapping("/TrendyPostList")
    public String trendy(Model model, HttpSession session) {
        return renderMain("trendy", "인기글", dashboardService.trendy(me(session)), model, session);
    }

    @GetMapping("/NewPostList")
    public String latest(Model model, HttpSession session) {
        return renderMain("new", "최신글", dashboardService.latest(me(session)), model, session);
    }

    @GetMapping("/FollowPostList")
    public String follow(Model model, HttpSession session) {
        return renderMain("follow", "구독한 글", dashboardService.follow(me(session)), model, session);
    }

    @GetMapping("/TrendythemePostList")
    public String theme(Model model, HttpSession session) {
        return renderMain("theme", "언어별 인기글", dashboardService.theme(me(session)), model, session);
    }

    @PostMapping("/SearchPostList")
    public String search(@RequestParam(defaultValue = "") String keyword,
                         Model model, HttpSession session) {
        return renderMain("search", "검색: " + keyword,
                dashboardService.search(keyword, me(session)), model, session);
    }

    /** 메인 뷰 공통 렌더링 — 탭 플래그 + posts */
    private String renderMain(String tab, String pageTitle, List<PostDto> posts,
                              Model model, HttpSession session) {
        String me = me(session);
        model.addAttribute("pageTitle",  pageTitle);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   usernameOf(me));
        model.addAttribute("isTrendy",   "trendy".equals(tab));
        model.addAttribute("isNew",      "new".equals(tab));
        model.addAttribute("isFollow",   "follow".equals(tab));
        model.addAttribute("isTheme",    "theme".equals(tab));
        model.addAttribute("posts",      posts);
        return "main";
    }

    // ═══════════════════════════════════════════════════════════════
    //  개인 피드
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/feed")
    public String feed(Model model, HttpSession session) {
        String me = me(session);
        model.addAttribute("pageTitle",  "내 피드");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   usernameOf(me));
        model.addAttribute("user",       feedService.user(me));
        model.addAttribute("myPosts",    feedService.myPosts(me));
        return "feed";
    }
}
