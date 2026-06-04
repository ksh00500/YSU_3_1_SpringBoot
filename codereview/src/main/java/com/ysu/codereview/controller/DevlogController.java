package com.ysu.codereview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class DevlogController {

    // ═══════════════════════════════════════════════════════════════
    //  메인 페이지
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/")
    public String main(@RequestParam(defaultValue = "trendy") String tab, Model model) {
        model.addAttribute("pageTitle",  "DevLog");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   "testuser");
        model.addAttribute("isTrendy",   "trendy".equals(tab));
        model.addAttribute("isNew",      "new".equals(tab));
        model.addAttribute("isFollow",   "follow".equals(tab));
        model.addAttribute("isTheme",    "theme".equals(tab));
        model.addAttribute("posts",      dummyPostList());
        return "main";
    }

    @PostMapping("/SearchPostList")
    public String search(@RequestParam(defaultValue = "") String keyword, Model model) {
        model.addAttribute("pageTitle",  "검색: " + keyword);
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   "testuser");
        model.addAttribute("isTrendy",   false);
        model.addAttribute("isNew",      false);
        model.addAttribute("isFollow",   false);
        model.addAttribute("isTheme",    false);
        model.addAttribute("posts",      dummyPostList());
        return "main";
    }

    // ═══════════════════════════════════════════════════════════════
    //  로그인
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
    //  회원가입
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
    //  글 상세
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
    //  댓글
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
    //  구인구직
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
    //  개인 피드
    // ═══════════════════════════════════════════════════════════════
    @GetMapping("/feed")
    public String feed(Model model) {
        UserDto user = new UserDto();
        user.id             = "testuser";
        user.followerCount  = 42;
        user.followingCount = 18;

        model.addAttribute("pageTitle",  "내 피드");
        model.addAttribute("isLoggedIn", true);
        model.addAttribute("username",   "testuser");
        model.addAttribute("user",       user);
        model.addAttribute("myPosts",    dummyMyPosts());
        return "feed";
    }

    // ═══════════════════════════════════════════════════════════════
    //  글 작성 / 수정
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

    @PostMapping("/PostAdd")
    public String postAdd(@RequestParam Map<String, String> params) {
        String method = params.getOrDefault("_method", "POST");
        String puid   = params.get("puid");
        switch (method) {
            case "PUT":    return "redirect:/post/" + puid;
            case "DELETE": return "redirect:/feed";
            default:       return "redirect:/";
        }
    }

    // ═══════════════════════════════════════════════════════════════
    //  더미 데이터
    // ═══════════════════════════════════════════════════════════════
    private List<PostDto> dummyPostList() {
        String[][] data = {
                {"post-1", "Python으로 만드는 간단한 AI 챗봇",  "tech",   "Python",     "devholic",  "128", "24"},
                {"post-2", "React 성능 최적화 실전 가이드",      "tech",   "JavaScript", "coder99",   "95",  "16"},
                {"post-3", "Rust로 웹서버 구축하기 — 실전편",    "tech",   "Rust",       "rustlover", "72",  "11"},
                {"post-4", "취업 준비생을 위한 포트폴리오 전략",  "career", "Java",       "jobhunter", "61",  "8"},
                {"post-5", "Go 고루틴 완전 이해하기",            "tech",   "Go",         "gopher",    "54",  "9"},
                {"post-6", "코드 리뷰 잘 받는 7가지 방법",       "career", "JavaScript", "seniordev", "48",  "5"},
        };
        List<PostDto> list = new ArrayList<>();
        for (String[] d : data) {
            PostDto p    = new PostDto();
            p.puid          = d[0];
            p.title         = d[1];
            p.postType      = d[2];
            p.themeLanguage = d[3];
            p.authorId      = d[4];
            p.createdAt     = "2025.06.04";
            p.suggestCount  = Integer.parseInt(d[5]);
            p.commentCount  = Integer.parseInt(d[6]);
            p.hasThumbnail  = false;
            list.add(p);
        }
        return list;
    }

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

    private List<PostDto> dummyMyPosts() {
        String[][] data = {
                {"post-1", "Python으로 만드는 간단한 AI 챗봇", "Python",     "2025.06.04"},
                {"post-2", "React 성능 최적화 실전 가이드",    "JavaScript", "2025.05.28"},
        };
        List<PostDto> list = new ArrayList<>();
        for (String[] d : data) {
            PostDto p       = new PostDto();
            p.puid          = d[0];
            p.title         = d[1];
            p.themeLanguage = d[2];
            p.createdAt     = d[3];
            list.add(p);
        }
        return list;
    }

    // ═══════════════════════════════════════════════════════════════
    //  DTO 클래스
    // ═══════════════════════════════════════════════════════════════
    public static class PostDto {
        public String  puid, title, postType, themeLanguage;
        public String  authorId, createdAt, content, thumbnailUrl;
        public int     suggestCount, commentCount;
        public boolean hasThumbnail, isSuggested;
    }

    public static class CommentDto {
        public String  cuid, authorId, content;
        public boolean isOwner;
    }

    public static class JobDto {
        public String                   puid, title, company, location;
        public String                   jobType, companyLogoUrl, companyInitial;
        public List<Map<String,String>> tags;
    }

    public static class UserDto {
        public String id;
        public int    followerCount, followingCount;
    }
}