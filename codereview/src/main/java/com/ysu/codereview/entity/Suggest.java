// [AI 활용] 이 파일은 Claude AI를 활용하여 작성되었습니다.
package com.ysu.codereview.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

// DB 테이블과 연결되는 추천 Entity
// 같은 사람이 같은 게시글에 중복 추천하지 못하도록 UniqueConstraint 설정
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"post_id", "nickname"}) // (게시글 + 닉네임) 조합은 유일해야 함
})
public class Suggest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id 자동 증가
    private Long id;

    // 추천은 게시글(Post)에 속함
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false)
    private String nickname; // 추천한 사람의 닉네임 (중복 추천 방지에 사용)
}
