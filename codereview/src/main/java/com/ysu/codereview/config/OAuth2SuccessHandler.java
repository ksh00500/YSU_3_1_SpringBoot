package com.ysu.codereview.config;

import com.ysu.codereview.entity.Account;
import com.ysu.codereview.repository.AccountRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AccountRepository accountRepository;
    private final JwtProvider jwtProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        Account account = accountRepository.findByLoginId(email).orElseGet(() -> {
            Account newAccount = new Account();
            newAccount.setUuid(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
            newAccount.setLoginId(email);
            newAccount.setPassword("");
            newAccount.setSocialLogin(true);
            return accountRepository.save(newAccount);
        });

        String token = jwtProvider.createToken(account.getUuid());
        Cookie cookie = new Cookie("jwt_token", token);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        response.sendRedirect("/TrendyPostList");
    }
}
