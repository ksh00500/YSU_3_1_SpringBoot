package com.ysu.codereview.repository;

import com.ysu.codereview.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/** PK(uuid) 조회는 findById(uuid) 사용 */
public interface AccountRepository extends JpaRepository<Account, String> {
}
