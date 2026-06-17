package com.ysu.codereview.entity;

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
    private String uuid;

    @Column(name = "ID", length = 20, nullable = false, unique = true)
    private String loginId;

    @Column(name = "PW", length = 255, nullable = false)
    private String password;

    @Column(name = "SocialLogin")
    private Boolean socialLogin = false;
}
