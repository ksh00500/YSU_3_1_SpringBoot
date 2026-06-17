package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

// 댓글
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @Column(length = 16)
    public String cuid;

    // 글
    @Column(length = 16)
    public String puid;

    // 작성자
    @Column(length = 16)
    public String uuid;

    @Lob
    public String content;

    public Comment() {}

    public Comment(String cuid, String puid, String uuid, String content) {
        this.cuid = cuid;
        this.puid = puid;
        this.uuid = uuid;
        this.content = content;
    }
}
