// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.dto;

import com.example.community.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// 게시글 정보를 Controller/View로 전달할 때 사용하는 데이터 전달 객체 (DTO)
// Entity를 직접 노출하지 않고 DTO를 통해 필요한 데이터만 전달함
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDto {

    private Long id;
    private String title;
    private String codeContent;
    private String language;
    private String nickname;
    private LocalDateTime createdAt;

    // Post Entity → PostDto 변환 메서드
    public static PostDto of(Post post) {
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getCodeContent(),
                post.getLanguage(),
                post.getNickname(),
                post.getCreatedAt()
        );
    }
}
