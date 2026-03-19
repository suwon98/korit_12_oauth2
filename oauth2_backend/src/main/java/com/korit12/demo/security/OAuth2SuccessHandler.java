package com.korit12.demo.security;

import com.korit12.demo.entity.User;
import com.korit12.demo.repository.UserRepository;
import com.korit12.demo.service.JwtService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

// 구글 로그인 성공 후에 호출되는 부분입니다. JWT 발급까지 다 하고 프론트엔드로 보내주는 부분입니다.
// 의문점 : JSON 응답이 아니라 왜 redirect를 쓰는가?
// OAuth2의 flow가 브라우저 리다이렉트 기반이라서 JSON 응답을 보낼수가 없습니다. URL 쿼리 파라미터에
// JWT를 담아 프론트엔드로 보내고, 프론트에서 파싱해야 합니다.
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private static final String FRONTEND_REDIRECT_URL = "http://localhost:5173/oauth2/callback";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();    // authentication을 OAuth2User로 다운캐스팅, getPrincipal() -> 인증 끝난 사용자
        String email = oAuth2User.getAttribute("email");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다 : " + email));
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        // 이제 프론트엔드로 리다이렉트 (토큰 + 사용자 정보를 쿼리 파라미터로 전달)
        // http://localhost:5173/oauth2/callback?token=eyJ...&email=kim1@gamil.com&name=김일&role=ROLE_USER
        String redirectUrl = FRONTEND_REDIRECT_URL
                + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                + "&email=" + URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8)
                + "&name=" + URLEncoder.encode(user.getName(), StandardCharsets.UTF_8)
                + "&role=" + URLEncoder.encode(user.getRole().name(), StandardCharsets.UTF_8);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}
