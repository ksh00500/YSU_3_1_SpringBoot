package com.ysu.codereview.controller;

import com.ysu.codereview.dto.CommentDto;
import com.ysu.codereview.dto.JobDto;
import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.repository.AccountRepository;
import com.ysu.codereview.service.DashboardService;
import com.ysu.codereview.service.FeedService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    // ═══════════════════════════════════════════════════════════════
    //  로그인 (타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("pageTitle",  "로그인");
        model.addAttribute("isLoggedIn", false);
        return "login";
    }

    @PostMapping("/TryLogin")
    public String tryLogin(@RequestParam String id, @RequestParam String pw, Model model) {
        if ("testuser".equals(id) && "1234".equals(pw)) {
            return "redirect:/";
        }
        model.addAttribute("pageTitle",    "로그인");
        model.addAttribute("isLoggedIn",   false);
        model.addAttribute("errorMessage", "아이디 또는 비밀번호가 틀렸습니다.");
        return "login";
    }

    // ═══════════════════════════════════════════════════════════════
    //  회원가입 (타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("pageTitle",  "회원가입");
        model.addAttribute("isLoggedIn", false);
        return "register";
    }

    @PostMapping("/TryRegister")
    public String tryRegister(@RequestParam String id, @RequestParam String pw,
                              @RequestParam String pwConfirm, Model model) {
        if (!pw.equals(pwConfirm)) {
            model.addAttribute("pageTitle",    "회원가입");
            model.addAttribute("isLoggedIn",   false);
            model.addAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
            return "register";
        }
        return "redirect:/login";
    }

    // ═══════════════════════════════════════════════════════════════
    //  글 상세 (타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/post/{puid}")
    public String postDetail(@PathVariable String puid, Model model) {
        model.addAttribute("pageTitle",    "글 상세");
        model.addAttribute("isLoggedIn",   true);
        model.addAttribute("username",     "testuser");
        model.addAttribute("post",         dummyPost(puid));
        model.addAttribute("comments",     dummyComments());
        model.addAttribute("relatedPosts", dummyRelatedPosts());
        return "post-detail";
    }

    @PostMapping("/Suggest")
    public String suggest(@RequestParam String puid) {
        return "redirect:/post/" + puid;
    }

    @PostMapping("/CancelSuggest")
    public String cancelSuggest(@RequestParam String puid) {
        return "redirect:/post/" + puid;
    }

    @GetMapping("/AicodeEdit")
    public String aiCodeEdit(@RequestParam String puid, Model model) {
        model.addAttribute("pageTitle",    "AI 코드 리팩토링");
        model.addAttribute("isLoggedIn",   true);
        model.addAttribute("username",     "testuser");
        model.addAttribute("post",         dummyPost(puid));
        model.addAttribute("comments",     dummyComments());
        model.addAttribute("relatedPosts", dummyRelatedPosts());
        return "post-detail";
    }

    // ═══════════════════════════════════════════════════════════════
    //  댓글 (타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @PostMapping("/CommentAdd")
    public String commentAdd(@RequestParam String puid, @RequestParam String content) {
        return "redirect:/post/" + puid;
    }

    @PostMapping("/CommentEdit")
    public String commentEdit(@RequestParam String cuid, @RequestParam String content,
                              @RequestParam String puid) {
        return "redirect:/post/" + puid;
    }

    @PostMapping("/CommentDel")
    public String commentDel(@RequestParam String cuid, @RequestParam String puid) {
        return "redirect:/post/" + puid;
    }

    // ═══════════════════════════════════════════════════════════════
    //  구인구직 (타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/jobs")
    public String jobBoard(Model model) {
        model.addAttribute("pageTitle",  "구인구직");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   "testuser");
        model.addAttribute("jobs",       dummyJobs());
        return "job-board";
    }

    // ═══════════════════════════════════════════════════════════════
    //  글 작성 / 수정 (작성·수정은 타 담당 — 더미 유지)
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/post-editor")
    public String postEditor(@RequestParam(required = false) String puid, Model model) {
        boolean isEdit = puid != null && !puid.isEmpty();
        model.addAttribute("pageTitle",    isEdit ? "글 수정" : "글 작성");
        model.addAttribute("isLoggedIn",   true);
        model.addAttribute("username",     "testuser");
        model.addAttribute("isEdit",       isEdit);
        model.addAttribute("isTech",       false);
        model.addAttribute("isCareer",     false);
        model.addAttribute("isQuestion",   false);
        model.addAttribute("isJob",        false);
        model.addAttribute("isPython",     false);
        model.addAttribute("isJavaScript", false);
        model.addAttribute("isJava",       false);
        model.addAttribute("isRust",       false);
        model.addAttribute("isGo",         false);

        if (isEdit) {
            PostDto post = dummyPost(puid);
            model.addAttribute("post",         post);
            model.addAttribute("isTech",       "tech".equals(post.postType));
            model.addAttribute("isCareer",     "career".equals(post.postType));
            model.addAttribute("isQuestion",   "question".equals(post.postType));
            model.addAttribute("isJob",        "job".equals(post.postType));
            model.addAttribute("isPython",     "Python".equals(post.themeLanguage));
            model.addAttribute("isJavaScript", "JavaScript".equals(post.themeLanguage));
            model.addAttribute("isJava",       "Java".equals(post.themeLanguage));
            model.addAttribute("isRust",       "Rust".equals(post.themeLanguage));
            model.addAttribute("isGo",         "Go".equals(post.themeLanguage));
        } else {
            model.addAttribute("post",   new PostDto());
            model.addAttribute("isTech", true);
        }
        return "post-editor";
    }

    /**
     * 등록/수정/삭제 진입점. 피드의 삭제(_method=DELETE)는 실제 삭제 수행.
     * 등록/수정 본문 처리는 타 담당(현재 redirect만).
     */
    @PostMapping("/PostAdd")
    public String postAdd(@RequestParam Map<String, String> params, HttpSession session) {
        String method = params.getOrDefault("_method", "POST");
        String puid   = params.get("puid");
        switch (method) {
            case "PUT":
                return "redirect:/post/" + puid;
            case "DELETE":
                feedService.deletePost(me(session), puid);
                return "redirect:/feed";
            default:
                return "redirect:/";
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  더미 데이터 (타 담당 페이지용 — 글상세/관련글/댓글/구인구직)
    // ═══════════════════════════════════════════════════════════════
    private PostDto dummyPost(String puid) {
        PostDto p       = new PostDto();
        p.puid          = puid != null ? puid : "post-1";
        p.title         = "Python으로 만드는 간단한 AI 챗봇";
        p.postType      = "tech";
        p.themeLanguage = "Python";
        p.authorId      = "devholic";
        p.createdAt     = "2025.06.04";
        p.suggestCount  = 128;
        p.isSuggested   = false;
        p.content       = "<h2>들어가며</h2>"
                + "<p>Python과 OpenAI API를 연동해서 간단한 챗봇을 만들어봅니다.</p>"
                + "<pre><code>import openai\n"
                + "client = openai.OpenAI(api_key='YOUR_KEY')\n"
                + "response = client.chat.completions.create(\n"
                + "    model='gpt-4o',\n"
                + "    messages=[{\"role\":\"user\",\"content\":\"안녕!\"}]\n"
                + ")\n"
                + "print(response.choices[0].message.content)</code></pre>"
                + "<p>이렇게 하면 기본 챗봇을 바로 사용할 수 있습니다.</p>";
        return p;
    }

    private List<PostDto> dummyRelatedPosts() {
        String[][] data = {
                {"post-5", "GPT API 완벽 정복 가이드",       "Python"},
                {"post-6", "LangChain으로 RAG 챗봇 만들기",  "Python"},
                {"post-7", "FastAPI + ML 모델 배포 자동화",   "Python"},
        };
        List<PostDto> list = new ArrayList<>();
        for (String[] d : data) {
            PostDto p       = new PostDto();
            p.puid          = d[0];
            p.title         = d[1];
            p.themeLanguage = d[2];
            list.add(p);
        }
        return list;
    }

    private List<CommentDto> dummyComments() {
        List<CommentDto> list = new ArrayList<>();
        Object[][] data = {
                {"cmt-1", "devholic",  "정말 유익한 글이에요!",            false},
                {"cmt-2", "testuser",  "직접 써봤는데 잘 됩니다 ㅎㅎ",     true},
                {"cmt-3", "coder99",   "API 키는 어디서 받나요?",           false},
        };
        for (Object[] d : data) {
            CommentDto c = new CommentDto();
            c.cuid     = (String)  d[0];
            c.authorId = (String)  d[1];
            c.content  = (String)  d[2];
            c.isOwner  = (Boolean) d[3];
            list.add(c);
        }
        return list;
    }

    private List<JobDto> dummyJobs() {
        List<JobDto> list = new ArrayList<>();
        Object[][] data = {
                {"job-1", "시니어 백엔드 개발자 채용", "테크스타트업", "서울", "정규직",
                        new String[]{"Node.js", "PostgreSQL"}, "T"},
                {"job-2", "프론트엔드 개발자 모집",    "IT 컴퍼니",   "원격", "계약직",
                        new String[]{"React", "TypeScript"},   "I"},
                {"job-3", "AI/ML 엔지니어 채용 공고",  "대기업",      "판교", "정규직",
                        new String[]{"Python", "PyTorch"},     "D"},
        };
        for (Object[] d : data) {
            JobDto j         = new JobDto();
            j.puid           = (String) d[0];
            j.title          = (String) d[1];
            j.company        = (String) d[2];
            j.location       = (String) d[3];
            j.jobType        = (String) d[4];
            j.companyInitial = (String) d[6];
            j.tags           = new ArrayList<>();
            for (String tag : (String[]) d[5]) {
                Map<String, String> t = new HashMap<>();
                t.put("name", tag);
                j.tags.add(t);
            }
            list.add(j);
        }
        return list;
    }
}
