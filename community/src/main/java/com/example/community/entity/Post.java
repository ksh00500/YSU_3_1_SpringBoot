// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.example.community.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

// DB 테이블과 연결되는 게시글 Entity
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동 증가
    private Long id;

    @Column(nullable = false)
    private String title;           // 게시글 제목

    @Column(nullable = false, columnDefinition = "TEXT")
    private String codeContent;     // 코드 내용 (TEXT 타입으로 긴 코드 저장 가능)

    @Column(nullable = false)
    private String language;        // 프로그래밍 언어 (Java, Python 등)

    @Column(nullable = false)
    private String nickname;        // 작성자 닉네임

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 시간

    // DB에 저장되기 직전에 자동으로 현재 시간을 넣어주는 메서드
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
