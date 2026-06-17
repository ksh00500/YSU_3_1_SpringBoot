package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 게시글 추천 (ERD: PostSuggests) */
@Entity
@Table(name = "post_suggests")
public class PostSuggests {

    @Id
    @Column(length = 16)
    public String suid;

    /** 추천 대상 글 (FK -> Posts.puid) */
    @Column(length = 16)
    public String puid;

    /** 추천한 사용자 (FK -> Account.uuid) */
    @Column(length = 16)
    public String uuid;

    /** ERD: IsCheak — 추천 활성 여부 */
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
