// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.service;

import com.example.community.dto.PostDto;
import com.example.community.entity.Post;
import com.example.community.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 게시글 관련 비즈니스 로직을 처리하는 Service
// Controller에서 호출하며, Repository를 통해 DB와 통신함
@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository; // DB 접근을 위한 Repository 주입

    // [GET /PostContents] 게시글 상세 조회
    // postId로 게시글을 찾고, 없으면 예외 발생
    public PostDto getPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));
        log.info("게시글 조회 성공: {}", post);
        return PostDto.of(post); // Entity → DTO 변환 후 반환
    }

    // [GET /RelatedPostList] 관련 게시글 목록 조회
    // 현재 게시글과 같은 언어(language)의 게시글을 최대 5개 반환
    public List<PostDto> getRelatedPosts(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        return postRepository.findByLanguageAndIdNot(post.getLanguage(), postId)
                .stream()
                .limit(5)
                .map(PostDto::of)
                .collect(Collectors.toList());
    }

    // [GET /AicodeEdit] AI 코드 리팩토링 결과 반환
    // 현재는 게시글 코드를 그대로 반환 (추후 AI API 연동 시 이 메서드에 추가)
    public String getAiRefactored(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));
        log.info("AI 리팩토링 요청: postId={}", postId);
        return "[AI 리팩토링 결과]\n언어: " + post.getLanguage() + "\n\n" + post.getCodeContent();
    }
}
