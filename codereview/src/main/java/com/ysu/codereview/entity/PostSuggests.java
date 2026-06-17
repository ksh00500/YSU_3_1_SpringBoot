package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 추천
@Entity
@Table(name = "post_suggests")
public class PostSuggests {

    @Id
    @Column(length = 16)
    public String suid;

    // 글
    @Column(length = 16)
    public String puid;

    // 유저
    @Column(length = 16)
    public String uuid;

    // 추천 여부
    @Column(name = "is_cheak")
    public boolean cheak;

    public PostSuggests() {}

    public PostSuggests(String suid, String puid, String uuid, boolean cheak) {
        this.suid = suid;
        this.puid = puid;
        this.uuid = uuid;
        this.cheak = cheak;
    }
}
