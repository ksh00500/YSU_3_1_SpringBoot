package com.project.user.repository;

import com.project.user.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    // 아이디 중복 체크 및 로그인 시 아이디로 회원 찾기
    Optional<Account> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
}