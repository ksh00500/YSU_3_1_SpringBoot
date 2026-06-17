package com.project.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
public class Account {

    @Id
    @Column(name = "UUID", length = 16)
    private String uuid; //자동 생성 16자리 고유번호

    @Column(name = "ID", length = 20, nullable = false, unique = true)
    private String loginId; //아이디

    @Column(name = "PW", length = 255, nullable = false)
    private String password; // 암호화된 비밀번호

    @Column(name = "SocialLogin")
    private Boolean socialLogin = false; // 기본값 일반가입(false)
}
