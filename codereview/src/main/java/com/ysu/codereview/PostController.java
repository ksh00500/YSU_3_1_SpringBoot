package com.ysu.codereview;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import java.util.Map;

@Controller
public class PostController {

    @GetMapping("/posts")
    public String list(Model model) {
        List<Map<String, String>> posts = List.of(
                Map.of("title", "이진탐색 코드 리뷰 부탁드려요", "language", "Java",   "author", "홍길동", "createdAt", "2026-06-01"),
                Map.of("title", "정렬 알고리즘 질문있습니다",   "language", "Python", "author", "김철수", "createdAt", "2026-06-02"),
                Map.of("title", "API 호출 함수 피드백",        "language", "JS",     "author", "이영희", "createdAt", "2026-06-03")
        );
        model.addAttribute("posts", posts);
        return "list";
    }

    @GetMapping("/posts/new")
    public String newPost() {
        return "new";
    }

    @GetMapping("/posts/edit")
    public String editPost(Model model) {
        Map<String, String> post = Map.of(
                "title", "이진탐색 코드 리뷰 부탁드려요",
                "language", "Java",
                "code", "public int binarySearch(int[] arr, int target) { ... }",
                "description", "시간복잡도가 맞는지 봐주세요",
                "author", "홍길동"
        );
        model.addAttribute("post", post);
        return "edit";
    }

    @GetMapping("/posts/detail")
    public String detail(Model model) {
        Map<String, String> post = Map.of(
                "title", "이진탐색 코드 리뷰 부탁드려요",
                "language", "Java",
                "code", "public int binarySearch(int[] arr, int target) {\n    // ...\n}",
                "description", "시간복잡도가 맞는지, 더 깔끔하게 쓸 방법이 있는지 봐주세요.",
                "author", "홍길동",
                "createdAt", "2026-06-01"
        );
        model.addAttribute("post", post);
        return "show";
    }
}