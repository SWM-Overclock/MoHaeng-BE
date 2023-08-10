package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.swmaestro.mohaeng.component.jwt.JwtTokenProvider;
import org.swmaestro.mohaeng.domain.user.KakaoUserInfo;
import org.swmaestro.mohaeng.domain.user.OAuth2UserInfo;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LoginResponse;
import org.swmaestro.mohaeng.dto.OAuthTokenResponse;
import org.swmaestro.mohaeng.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OAuthService {
    private static final String BEARER_TYPE = "Bearer";

    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * @InMemoryRepository application-oauth properties 정보를 담고 있음
     * @param provider OAuth2 제공자
     * @param token OAuth2 제공자로부터 받은 Token
     * Kakao의 경우 Token을 이용하여 User 정보를 요청
     * @getUserProfile Oauth 서버에 Token을 이용하여 User 정보 요청
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse login(String provider, String token) {

        log.info("in OAuthService");
        ClientRegistration clientRegistration = inMemoryRepository.findByRegistrationId(provider);
        User user = getUserProfile(provider, token, clientRegistration);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(user.getId()));
        String refreshToken = jwtTokenProvider.createRefreshToken();


        return LoginResponse.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .tokenType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * @param provider OAuth2 제공자
     * @param token OAuth2 제공자로부터 받은 Token
     * @param clientRegistration OAuth2 제공자 정보
     * @getUserAttributes Oauth 서버에 Token을 이용하여 User 정보 요청
     * @return User 정보
     */
    private User getUserProfile(String provider, String token, ClientRegistration clientRegistration) {
        Map<String, Object> userAttributes = getUserAttributes(clientRegistration, token);
        OAuth2UserInfo oAuth2UserInfo = null;
        if (provider.equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(userAttributes);
        } else {
            log.info("허용되지 않은 Provider");
        }

        String provide = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String nickname = oAuth2UserInfo.getNickName();
        String email = oAuth2UserInfo.getEmail();
        String imageUrl = oAuth2UserInfo.getImageUrl();

        User userEntity = userRepository.findByEmail(email);

        if (userEntity == null) {
            userEntity = User.createUser(email, nickname, provide, providerId, imageUrl);
            userRepository.save(userEntity);
        }
        return userEntity;
    }

    private Map<String, Object> getUserAttributes(ClientRegistration provider, String token) {
        log.info("getUserAttributes In");
        log.info("userinfoUri = {}", provider.getProviderDetails().getUserInfoEndpoint().getUri());
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(token))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
