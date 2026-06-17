package com.ysu.codereview.service;

import com.ysu.codereview.config.JwtProvider;
import com.ysu.codereview.dto.LoginRequest;
import com.ysu.codereview.dto.SignupRequest;
import com.ysu.codereview.entity.Account;
import com.ysu.codereview.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public String registerAccount(SignupRequest request) {
        if (accountRepository.existsByLoginId(request.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Account account = new Account();
        account.setLoginId(request.getId());
        account.setPassword(passwordEncoder.encode(request.getPw()));
        account.setUuid(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        account.setSocialLogin(false);

        accountRepository.save(account);
        return "회원가입 성공!";
    }

    public String login(LoginRequest request) {
        Account account = accountRepository.findByLoginId(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        if (!passwordEncoder.matches(request.getPw(), account.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtProvider.createToken(account.getUuid());
    }
}
