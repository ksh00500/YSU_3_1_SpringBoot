package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 구독
@Entity
@Table(name = "follows")
public class Follows {

    @Id
    @Column(length = 16)
    public String fuid;

    // 구독자
    @Column(length = 16)
    public String followerId;

    // 대상
    @Column(length = 16)
    public String followingId;

    public Follows() {}

    public Follows(String fuid, String followerId, String followingId) {
        this.fuid = fuid;
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
