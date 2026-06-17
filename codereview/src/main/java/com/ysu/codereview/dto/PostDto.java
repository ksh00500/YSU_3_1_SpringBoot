package com.ysu.codereview.dto;

import com.ysu.codereview.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {

    private Long puid;
    private String title;
    private String content;        // Post.codeContent
    private String themeLanguage;  // Post.language
    private String postType;
    private String authorId;       // Post.nickname
    private long suggestCount;
    private long commentCount;
    private LocalDateTime createdAt;

    public static PostDto of(Post post, long suggestCount, long commentCount) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getCodeContent(),
                post.getLanguage(),
                post.getPostType(),
                post.getNickname(),
                suggestCount,
                commentCount,
                post.getCreatedAt()
        );
    }

    public static PostDto of(Post post) {
        return of(post, 0, 0);
    }
}
