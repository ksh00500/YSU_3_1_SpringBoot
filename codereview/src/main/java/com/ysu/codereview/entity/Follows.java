package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** 구독 (ERD: Follows) */
@Entity
@Table(name = "follows")
public class Follows {

    @Id
    @Column(length = 16)
    public String fuid;

    /** 구독하는 사람 (Account.uuid) */
    @Column(length = 16)
    public String followerId;

    /** 구독 당하는 사람 (Account.uuid) */
    @Column(length = 16)
    public String followingId;

    public Follows() {}

    public Follows(String fuid, String followerId, String followingId) {
        this.fuid = fuid;
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
