// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.service;

import com.example.community.entity.Post;
import com.example.community.entity.Suggest;
import com.example.community.repository.PostRepository;
import com.example.community.repository.SuggestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 추천 관련 비즈니스 로직을 처리하는 Service
@Slf4j
@Service
public class SuggestService {

    @Autowired
    private SuggestRepository suggestRepository; // 추천 DB 접근

    @Autowired
    private PostRepository postRepository; // 추천할 게시글 확인용

    // [POST /Suggest] 추천 등록
    // 이미 추천한 경우 중복 추천을 막고 안내 메시지 반환
    @Transactional
    public String addSuggest(Long postId, String nickname) {
        // 이미 추천한 기록이 있으면 중복 추천 차단
        if (suggestRepository.existsByPostIdAndNickname(postId, nickname)) {
            return "이미 추천한 게시글입니다.";
        }

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. id=" + postId));

        Suggest suggest = new Suggest(null, post, nickname);
        suggestRepository.save(suggest);
        log.info("추천 등록: postId={}, nickname={}", postId, nickname);
        return "추천 완료";
    }

    // [POST /CancelSuggest] 추천 취소
    // 추천 기록을 찾아서 삭제
    @Transactional
    public String cancelSuggest(Long postId, String nickname) {
        Suggest suggest = suggestRepository.findByPostIdAndNickname(postId, nickname)
                .orElseThrow(() -> new IllegalArgumentException("추천 기록이 없습니다."));

        suggestRepository.delete(suggest);
        log.info("추천 취소: postId={}, nickname={}", postId, nickname);
        return "추천 취소 완료";
    }

    // [GET /SuggestCount] 추천 수 조회
    // 해당 게시글의 총 추천 수를 반환
    public long getSuggestCount(Long postId) {
        return suggestRepository.countByPostId(postId);
    }
}
