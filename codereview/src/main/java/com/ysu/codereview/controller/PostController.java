// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.controller;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// MVC 패턴의 Controller: 요청을 받아 Service에 처리를 맡기고 View(HTML)를 반환함
// @Controller = 뷰(HTML 페이지)를 반환하는 컨트롤러
@Slf4j
@Controller
public class PostController {

    @Autowired
    private PostService postService; // 비즈니스 로직 처리를 Service에 위임

    // [GET /PostContents?postId=1] 글 상세 페이지 반환
    // 흐름: 요청 → Controller → Service → Repository → DB → View
    @GetMapping("/PostContents")
    public String postContents(@RequestParam Long postId, Model model) {
        log.info("글 상세 페이지 요청: postId={}", postId);

        PostDto post = postService.getPost(postId); // Service에서 게시글 데이터 가져옴
        model.addAttribute("post", post);           // View에 데이터 전달

        return "post/detail"; // templates/post/detail.html 렌더링
    }
}
