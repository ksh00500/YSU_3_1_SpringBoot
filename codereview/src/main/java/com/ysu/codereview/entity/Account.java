package com.ysu.codereview.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 사용자
@Entity
@Table(name = "account")
public class Account {

    @Id
    @Column(length = 16)
    public String uuid;

    @Column(length = 20)
    public String id;

    @Column(length = 255)
    public String pw;

    public boolean socialLogin;

    public Account() {}

    public Account(String uuid, String id, String pw, boolean socialLogin) {
        this.uuid = uuid;
        this.id = id;
        this.pw = pw;
        this.socialLogin = socialLogin;
    }
}
