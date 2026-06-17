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

// 대시보드/피드
@Controller
public class DevlogController {

    // 임시 유저
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

    // 현재 유저
    private String me(HttpSession session) {
        Object u = session.getAttribute("loginUuid");
        return u != null ? u.toString() : DEMO_USER;
    }

    // 이름
    private String usernameOf(String uuid) {
        return accountRepo.findById(uuid).map(a -> a.id).orElse("guest");
    }

    // 메인
    @GetMapping("/")
    public String main(Model model, HttpSession session) {
        return renderMain("trendy", "DevLog", dashboardService.trendy(me(session)), model, session);
    }

    // 인기글
    @GetMapping("/TrendyPostList")
    public String trendy(Model model, HttpSession session) {
        return renderMain("trendy", "인기글", dashboardService.trendy(me(session)), model, session);
    }

    // 최신글
    @GetMapping("/NewPostList")
    public String latest(Model model, HttpSession session) {
        return renderMain("new", "최신글", dashboardService.latest(me(session)), model, session);
    }

    // 구독글
    @GetMapping("/FollowPostList")
    public String follow(Model model, HttpSession session) {
        return renderMain("follow", "구독한 글", dashboardService.follow(me(session)), model, session);
    }

    // 언어별
    @GetMapping("/TrendythemePostList")
    public String theme(Model model, HttpSession session) {
        return renderMain("theme", "언어별 인기글", dashboardService.theme(me(session)), model, session);
    }

    // 검색
    @PostMapping("/SearchPostList")
    public String search(@RequestParam(defaultValue = "") String keyword,
                         Model model, HttpSession session) {
        return renderMain("search", "검색: " + keyword,
                dashboardService.search(keyword, me(session)), model, session);
    }

    // 공통 렌더
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

    // 피드
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
