//AI의 도움을 받아 작성되었습니다
package com.project.user.service;

import com.project.user.config.JwtProvider;
import com.project.user.dto.LoginRequest;
import com.project.user.dto.SignupRequest;
import com.project.user.entity.Account;
import com.project.user.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider; // 토큰 기계 불러오기

    public String registerAccount(SignupRequest request) {
        if (accountRepository.existsByLoginId(request.getId())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Account account = new Account();
        account.setLoginId(request.getId());
        account.setPassword(passwordEncoder.encode(request.getPw())); // 비밀번호 암호화

        // 16자리 UUID 자동 생성
        String generatedUuid = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        account.setUuid(generatedUuid);
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

        // 로그인 성공! 유저의 UUID로 토큰을 만들어서 반환합니다.
        return jwtProvider.createToken(account.getUuid());
    }
}