package com.korit12.demo.service;

import com.korit12.demo.entity.User;
import com.korit12.demo.repository.OAuth2UserRepository;
import com.korit12.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final OAuth2UserRepository oAuth2UserRepository;

    // 1. 부모 클래스로 구글 사용자 정보 조회(Access Token 사용)
    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();  // "google"
        String accessToken = userRequest.getAccessToken().getTokenValue();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 이상의 단계에서 구글에서 받아오는 정보의 예시
        // { "sub" : "123456...", "name" : "김일", "email" : "kim1@test.com", "picture" : "..." }
        String providerId = (String)attributes.get("sub");
        String email = (String)attributes.get("email");
        String name = (String)attributes.get("name");

        // DB에서 소셜 연동 정보 조회
        com.korit12.demo.entity.OAuth2User savedOAuth2User = oAuth2UserRepository
                .findByProviderAndProviderId(provider, providerId)
                .orElse(null);
        User user;

        if(savedOAuth2User == null) {
            // 최소 소셜 로그인 -> 자동 회원 가입으로 넘겨야 함.
            user = userRepository.findByEmail(email)
                    .orElseGet(() -> userRepository.save(User.createOAuth2User(email, name)));

            oAuth2UserRepository.save(com.korit12.demo.entity.OAuth2User
                    .create(user, provider, providerId, accessToken));
        } else {
            // 기존 소셜 로그인이 가능한 사람이면 -> Access Token만 갱신
            savedOAuth2User.updateAccessToken(accessToken);
            user = savedOAuth2User.getUser();
        }
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())),
                attributes,
                "email"
        );
    }
}
