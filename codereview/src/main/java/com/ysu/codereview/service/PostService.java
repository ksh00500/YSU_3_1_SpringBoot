package com.ysu.codereview.service;

import com.ysu.codereview.dto.PostDto;
import com.ysu.codereview.entity.Post;
import com.ysu.codereview.repository.CommentRepository;
import com.ysu.codereview.repository.PostRepository;
import com.ysu.codereview.repository.SuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SuggestRepository suggestRepository;

    @Autowired
    private CommentRepository commentRepository;

    private PostDto toDto(Post post) {
        long sc = suggestRepository.countByPostId(post.getId());
        long cc = commentRepository.countByPostId(post.getId());
        return PostDto.of(post, sc, cc);
    }

    public PostDto getPost(Long puid) {
        Post post = postRepository.findById(puid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + puid));
        return toDto(post);
    }

    public List<PostDto> getTrendingPosts() {
        return postRepository.findAll().stream()
                .map(this::toDto)
                .sorted(Comparator.comparingLong(PostDto::getSuggestCount).reversed())
                .collect(Collectors.toList());
    }

    public List<PostDto> getAllPostsByDate() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getPostsByTheme(String themeLanguage) {
        return postRepository.findByLanguageOrderByCreatedAtDesc(themeLanguage).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getPostsByKeyword(String keyword) {
        return postRepository.findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(keyword).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getMyPosts(String authorId) {
        return postRepository.findByNicknameOrderByCreatedAtDesc(authorId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getJobPosts() {
        return postRepository.findByPostTypeOrderByCreatedAtDesc("job").stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<PostDto> getRelatedPosts(Long puid) {
        Post post = postRepository.findById(puid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + puid));
        return postRepository.findByLanguageAndIdNot(post.getLanguage(), puid).stream()
                .limit(5)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto createPost(String title, String content, String themeLanguage, String postType, String authorId) {
        Post post = new Post(null, title, content, themeLanguage, authorId, postType, null);
        Post saved = postRepository.save(post);
        log.info("게시글 등록: {}", saved);
        return PostDto.of(saved);
    }

    @Transactional
    public PostDto updatePost(Long puid, String title, String content, String themeLanguage, String postType) {
        Post post = postRepository.findById(puid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + puid));
        post.update(title, content, themeLanguage, postType);
        Post saved = postRepository.save(post);
        log.info("게시글 수정: {}", saved);
        return toDto(saved);
    }

    @Transactional
    public void deletePost(Long puid) {
        Post post = postRepository.findById(puid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + puid));
        postRepository.delete(post);
        log.info("게시글 삭제: puid={}", puid);
    }

    public String getAiRefactored(Long puid) {
        Post post = postRepository.findById(puid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + puid));
        return "[AI 리팩토링 결과]\n언어: " + post.getLanguage() + "\n\n" + post.getCodeContent();
    }
}
