// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// DB 테이블과 연결되는 댓글 Entity
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동 증가
    private Long id;

    // 댓글은 게시글(Post)에 속함 → 여러 댓글이 하나의 게시글에 달릴 수 있음
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String nickname;        // 댓글 작성자 닉네임

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;            // 댓글 내용

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 시간

    // DB에 저장되기 직전에 자동으로 현재 시간을 넣어주는 메서드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 댓글 수정 시 내용만 변경 (null이거나 빈 값이면 변경하지 않음)
    public void updateBody(String body) {
        if (body != null && !body.isBlank()) {
            this.body = body;
        }
    }
}
