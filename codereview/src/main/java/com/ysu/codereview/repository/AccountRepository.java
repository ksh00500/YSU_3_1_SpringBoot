package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

// 계정
public interface AccountRepository extends JpaRepository<Account, String> {
}
