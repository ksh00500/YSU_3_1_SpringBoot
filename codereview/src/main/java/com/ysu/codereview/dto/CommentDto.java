package com.ysu.codereview.dto;

import com.ysu.codereview.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommentDto {

    private Long cuid;
    private Long postId;
    private String authorId;   // Comment.nickname
    private String content;    // Comment.body
    private LocalDateTime createdAt;
    private boolean isOwner;

    public static CommentDto of(Comment comment, boolean isOwner) {
        return new CommentDto(
                comment.getId(),
                comment.getPost().getId(),
                comment.getNickname(),
                comment.getBody(),
                comment.getCreatedAt(),
                isOwner
        );
    }

    public static CommentDto of(Comment comment) {
        return of(comment, false);
    }
}
