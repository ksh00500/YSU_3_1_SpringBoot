package com.ysu.codereview.controller;

import com.ysu.codereview.config.JwtProvider;
import com.ysu.codereview.dto.CommentDto;
import com.ysu.codereview.dto.LoginRequest;
import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.dto.SignupRequest;
import com.ysu.codereview.entity.Account;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.service.AccountService;
import com.ysu.codereview.service.AiService;
import com.ysu.codereview.service.CommentService;
import com.ysu.codereview.service.PostService;
import com.ysu.codereview.service.SuggestService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class PostController {

    @Autowired private PostService postService;
    @Autowired private CommentService commentService;
    @Autowired private AccountService accountService;
    @Autowired private SuggestService suggestService;
    @Autowired private AccountRepository accountRepository;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private AiService aiService;

    private Account getAccountFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("jwt_token".equals(cookie.getName())) {
                try {
                    String uuid = jwtProvider.getUuid(cookie.getValue());
                    return accountRepository.findById(uuid).orElse(null);
                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }

    private void addAuthModel(Model model, Account account) {
        model.addAttribute("isLoggedIn", account != null);
        model.addAttribute("username", account != null ? account.getLoginId() : "");
    }

    // ── 인증 ────────────────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle", "로그인");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle", "회원가입");
        return "register";
    }

    @PostMapping("/TryLogin")
    public String tryLogin(@RequestParam String id, @RequestParam String pw,
                           HttpServletResponse response, Model model) {
        LoginRequest req = new LoginRequest();
        req.setId(id);
        req.setPw(pw);
        try {
            String token = accountService.login(req);
            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setPath("/");
            cookie.setMaxAge(3600);
            response.addCookie(cookie);
            return "redirect:/TrendyPostList";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "login";
        }
    }

    @PostMapping("/TryRegister")
    public String tryRegister(@RequestParam String id, @RequestParam String pw,
                              @RequestParam String pwConfirm, Model model) {
        if (!pw.equals(pwConfirm)) {
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "register";
        }
        SignupRequest req = new SignupRequest();
        req.setId(id);
        req.setPw(pw);
        try {
            accountService.registerAccount(req);
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/TrendyPostList";
    }

    // ── 메인 ────────────────────────────────────────────────────────────

    private void addMainModel(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        addAuthModel(model, account);
        model.addAttribute("trendingPosts", postService.getTrendingPosts());
        model.addAttribute("newPosts", postService.getAllPostsByDate());
        model.addAttribute("pythonPosts", postService.getPostsByTheme("Python"));
        model.addAttribute("jsPosts", postService.getPostsByTheme("JavaScript"));
        model.addAttribute("javaPosts", postService.getPostsByTheme("Java"));
        model.addAttribute("jobPosts", postService.getJobPosts());
        model.addAttribute("pageTitle", "DevLog");
    }

    @GetMapping("/")
    public String root(Model model, HttpServletRequest request) {
        addMainModel(model, request);
        return "main";
    }

    @GetMapping("/TrendyPostList")
    public String trendyPostList(Model model, HttpServletRequest request) {
        addMainModel(model, request);
        return "main";
    }

    @GetMapping("/NewPostList")
    public String newPostList(Model model, HttpServletRequest request) {
        addMainModel(model, request);
        return "main";
    }

    @GetMapping("/TrendythemePostList")
    public String trendythemePostList(@RequestParam(defaultValue = "") String lang,
                                      Model model, HttpServletRequest request) {
        addMainModel(model, request);
        return "main";
    }

    @GetMapping("/FollowPostList")
    public String followPostList(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addMainModel(model, request);
        return "main";
    }

    @PostMapping("/SearchPostList")
    public String searchPostList(@RequestParam String keyword, Model model,
                                 HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        addAuthModel(model, account);
        model.addAttribute("searchPosts", postService.getPostsByKeyword(keyword));
        model.addAttribute("keyword", keyword);
        model.addAttribute("isSearch", true);
        model.addAttribute("pageTitle", "검색: " + keyword);
        return "main";
    }

    // ── 게시글 ────────────────────────────────────────────────────────────

    @GetMapping("/PostContents")
    public String postContents(@RequestParam Long puid, Model model,
                               HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        addAuthModel(model, account);

        PostDto post = postService.getPost(puid);
        String currentLoginId = account != null ? account.getLoginId() : null;
        List<CommentDto> comments = commentService.getComments(puid, currentLoginId);
        List<PostDto> relatedPosts = postService.getRelatedPosts(puid);

        boolean isSuggested = account != null && suggestService.hasSuggested(puid, account.getLoginId());
        boolean isPostAuthor = account != null && account.getLoginId().equals(post.getAuthorId());

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedPosts", relatedPosts);
        model.addAttribute("isSuggested", isSuggested);
        model.addAttribute("isPostAuthor", isPostAuthor);
        model.addAttribute("pageTitle", post.getTitle());
        return "post-detail";
    }

    @GetMapping("/post-editor")
    public String postEditor(@RequestParam(required = false) Long puid,
                             Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addAuthModel(model, account);

        if (puid != null) {
            PostDto post = postService.getPost(puid);
            model.addAttribute("post", post);
            model.addAttribute("isEdit", true);
            model.addAttribute("pageTitle", "글 수정");
            model.addAttribute("isTech",       "tech".equals(post.getPostType()));
            model.addAttribute("isCareer",     "career".equals(post.getPostType()));
            model.addAttribute("isQuestion",   "question".equals(post.getPostType()));
            model.addAttribute("isJob",        "job".equals(post.getPostType()));
            model.addAttribute("isPython",     "Python".equals(post.getThemeLanguage()));
            model.addAttribute("isJavaScript", "JavaScript".equals(post.getThemeLanguage()));
            model.addAttribute("isJava",       "Java".equals(post.getThemeLanguage()));
            model.addAttribute("isRust",       "Rust".equals(post.getThemeLanguage()));
            model.addAttribute("isGo",         "Go".equals(post.getThemeLanguage()));
        } else {
            model.addAttribute("isEdit", false);
            model.addAttribute("pageTitle", "새 글 쓰기");
            model.addAttribute("isTech", false);
            model.addAttribute("isCareer", false);
            model.addAttribute("isQuestion", false);
            model.addAttribute("isJob", false);
            model.addAttribute("isPython", false);
            model.addAttribute("isJavaScript", false);
            model.addAttribute("isJava", false);
            model.addAttribute("isRust", false);
            model.addAttribute("isGo", false);
        }
        return "post-editor";
    }

    @PostMapping("/PostAdd")
    public String postAdd(@RequestParam(required = false) String _method,
                          @RequestParam(required = false) Long puid,
                          @RequestParam(required = false) String title,
                          @RequestParam(required = false) String content,
                          @RequestParam(required = false) String themeLanguage,
                          @RequestParam(required = false) String postType,
                          HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";

        if ("DELETE".equals(_method) && puid != null) {
            postService.deletePost(puid);
            return "redirect:/feed";
        } else if ("PUT".equals(_method) && puid != null) {
            postService.updatePost(puid, title, content, themeLanguage, postType);
            return "redirect:/PostContents?puid=" + puid;
        } else {
            PostDto created = postService.createPost(title, content, themeLanguage, postType, account.getLoginId());
            return "redirect:/PostContents?puid=" + created.getPuid();
        }
    }

    // ── 피드 ────────────────────────────────────────────────────────────

    @GetMapping("/feed")
    public String feed(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addAuthModel(model, account);
        Map<String, Object> user = new HashMap<>();
        user.put("id", account.getLoginId());
        user.put("followerCount", 0);
        user.put("followingCount", 0);
        model.addAttribute("user", user);
        model.addAttribute("myPosts", postService.getMyPosts(account.getLoginId()));
        model.addAttribute("pageTitle", "내 글 목록");
        return "feed";
    }

    // ── AI 코드 리팩토링 ──────────────────────────────────────────────

    @GetMapping("/AicodeEdit")
    public String aiCodeEdit(@RequestParam Long puid, Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addAuthModel(model, account);

        PostDto post = postService.getPost(puid);
        model.addAttribute("post", post);
        model.addAttribute("pageTitle", "AI 코드 리팩토링");

        String aiResult = aiService.refactorCode(post.getContent(), post.getThemeLanguage());
        model.addAttribute("aiResult", aiResult);

        return "ai-refactor";
    }

    // ── job-board / AI / 설정 ──────────────────────────────────────────

    @GetMapping("/job-board")
    public String jobBoard(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        addAuthModel(model, account);
        model.addAttribute("jobs", postService.getJobPosts());
        model.addAttribute("pageTitle", "구인구직");
        return "job-board";
    }

@GetMapping("/feed/follows")
    public String feedFollows(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addAuthModel(model, account);
        model.addAttribute("myPosts", List.of());
        model.addAttribute("pageTitle", "구독 관리");
        return "feed";
    }

    @GetMapping("/settings")
    public String settings(Model model, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        addAuthModel(model, account);
        model.addAttribute("pageTitle", "설정");
        return "feed";
    }

    // ── 댓글 / 추천 ────────────────────────────────────────────────────

    @PostMapping("/CommentAdd")
    public String commentAdd(@RequestParam Long puid, @RequestParam String content,
                             HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        commentService.addComment(puid, account.getLoginId(), content);
        return "redirect:/PostContents?puid=" + puid;
    }

    @PostMapping("/Suggest")
    public String suggest(@RequestParam Long puid, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        suggestService.addSuggest(puid, account.getLoginId());
        return "redirect:/PostContents?puid=" + puid;
    }

    @PostMapping("/CancelSuggest")
    public String cancelSuggest(@RequestParam Long puid, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        suggestService.cancelSuggest(puid, account.getLoginId());
        return "redirect:/PostContents?puid=" + puid;
    }

    @PostMapping("/CommentEdit")
    public String commentEdit(@RequestParam Long cuid, @RequestParam Long puid,
                              @RequestParam String content, HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        commentService.editComment(cuid, content);
        return "redirect:/PostContents?puid=" + puid;
    }

    @PostMapping("/CommentDel")
    public String commentDel(@RequestParam Long cuid, @RequestParam Long puid,
                             HttpServletRequest request) {
        Account account = getAccountFromCookie(request);
        if (account == null) return "redirect:/login";
        commentService.deleteComment(cuid);
        return "redirect:/PostContents?puid=" + puid;
    }
}
