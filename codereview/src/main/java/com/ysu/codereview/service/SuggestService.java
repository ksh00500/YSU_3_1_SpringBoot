package com.ysu.codereview.service;

import com.ysu.codereview.entity.Post;
import com.ysu.codereview.entity.Suggest;
import com.ysu.codereview.repository.PostRepository;
import com.ysu.codereview.repository.SuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SuggestService {

    @Autowired
    private SuggestRepository suggestRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public String addSuggest(Long postId, String nickname) {
        if (suggestRepository.existsByPostIdAndNickname(postId, nickname)) {
            return "이미 추천한 게시글입니다.";
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));
        suggestRepository.save(new Suggest(null, post, nickname));
        log.info("추천 등록: postId={}, nickname={}", postId, nickname);
        return "추천 완료";
    }

    @Transactional
    public String cancelSuggest(Long postId, String nickname) {
        Suggest suggest = suggestRepository.findByPostIdAndNickname(postId, nickname)
                .orElseThrow(() -> new IllegalArgumentException("추천 기록이 없습니다."));
        suggestRepository.delete(suggest);
        log.info("추천 취소: postId={}, nickname={}", postId, nickname);
        return "추천 취소 완료";
    }

    public long getSuggestCount(Long postId) {
        return suggestRepository.countByPostId(postId);
    }

    public boolean hasSuggested(Long postId, String nickname) {
        return suggestRepository.existsByPostIdAndNickname(postId, nickname);
    }
}
