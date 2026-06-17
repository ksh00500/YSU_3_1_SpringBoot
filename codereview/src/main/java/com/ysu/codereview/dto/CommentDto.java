// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.dto;

import com.ysu.codereview.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 댓글 정보를 Controller/View로 전달할 때 사용하는 데이터 전달 객체 (DTO)
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private Long id;
    private Long postId;      // 어떤 게시글의 댓글인지
    private String nickname;  // 작성자
    private String body;      // 댓글 내용
    private LocalDateTime createdAt;

    // Comment Entity → CommentDto 변환 메서드
    public static CommentDto of(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getNickname(),
                comment.getBody(),
                comment.getCreatedAt()
        );
    }
}
