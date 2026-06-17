package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.time.LocalDate;

/** 게시글 (ERD: Posts) */
@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @Column(length = 16)
    public String puid;

    /** 작성자 (FK -> Account.uuid) */
    @Column(length = 16)
    public String uuid;

    @Column(length = 32)
    public String postType;

    @Column(length = 124)
    public String title;

    @Lob
    public String content;

    @Column(length = 32)
    public String themeLanguage;

    public LocalDate createdAt;

    public Posts() {}

    public Posts(String puid, String uuid, String postType, String title,
                 String content, String themeLanguage, LocalDate createdAt) {
        this.puid = puid;
        this.uuid = uuid;
        this.postType = postType;
        this.title = title;
        this.content = content;
        this.themeLanguage = themeLanguage;
        this.createdAt = createdAt;
    }
}
