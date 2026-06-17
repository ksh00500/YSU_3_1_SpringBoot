package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// 계정
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);
}
