package org.swmaestro.mohaeng.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.swmaestro.mohaeng.component.jwt.JwtTokenProvider;
import org.swmaestro.mohaeng.domain.user.KakaoUserInfo;
import org.swmaestro.mohaeng.domain.user.OAuth2UserInfo;
import org.swmaestro.mohaeng.domain.user.User;
import org.swmaestro.mohaeng.dto.LoginResponse;
import org.swmaestro.mohaeng.repository.UserRepository;

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
     * 로그인 처리를 위한 메서드
     * OAuth2 제공자로부터 받은 토큰을 사용하여 사용자 프로필을 가져오고,
     * 해당 사용자를 데이터베읏에서 찾거나 생성한 후 JWT 토큰을 발급합니다.
     *
     * @param provider OAuth2 제공자 (ex: "kakao")
     * @param token OAuth2 제공자로부터 받은 토큰
     * @return 로그인 정보를 포함하는 LoginResponse 객체
     */
    @Transactional
    public LoginResponse login(String provider, String token) {
        log.info("OAuth Service login initiated");

        ClientRegistration clientRegistration = inMemoryRepository.findByRegistrationId(provider);
        User user = getUserProfile(provider, token, clientRegistration);

        String accessToken = jwtTokenProvider.createAccessToken(String.valueOf(user.getEmail()));
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
     * OAuth2 제공자로부터 사용자 정보를 가져옵니다.
     * 해당 사용자가 데이터베이스에 없으면 새 사용자를 생성합니다.
     *
     * @param provider OAuth2 제공자 이름
     * @param token OAuth2 제공자로부터 받은 토큰
     * @param clientRegistration OAuth2 제공자에 대한 정보
     * @return 데이터베이스의 사용자 객체
     */
    private User getUserProfile(String provider, String token, ClientRegistration clientRegistration) {
        OAuth2UserInfo oAuth2UserInfo = retrieveOAuth2UserInfo(provider, getUserAttributes(clientRegistration, token));
        return findOrCreateUser(oAuth2UserInfo);
    }

    /**
     * 제공된 사용자 속성을 기반으로 OAuth2UserInfo 객체를 반환합니다.
     * 현재는 "kakao"만 지원됩니다.
     *
     * @param provider OAuth2 제공자 이름
     * @param userAttributes OAuth2 제공자로부터 얻은 사용자 속성
     * @return OAuth2 사용자 정보
     */
    private OAuth2UserInfo retrieveOAuth2UserInfo(String provider, Map<String, Object> userAttributes) {
        if ("kakao".equals(provider)) {
            return new KakaoUserInfo(userAttributes);
        }
        log.error("Unsupported provider: {}", provider);
        throw new IllegalArgumentException("Unsupported provider: " + provider);
    }

    /**
     * 주어진 OAuth2UserInfo를 기반으로 데이터베이스에서 사용자를 찾거나 생성합니다.
     *
     * @param oAuth2UserInfo OAuth2 사용자 정보
     * @return 데이터베이스의 사용자 객체
     */
    private User findOrCreateUser(OAuth2UserInfo oAuth2UserInfo) {
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        if (user == null) {
            user = User.createUser(
                    oAuth2UserInfo.getEmail(),
                    oAuth2UserInfo.getNickName(),
                    oAuth2UserInfo.getProvider(),
                    oAuth2UserInfo.getProviderId(),
                    oAuth2UserInfo.getImageUrl());
            userRepository.save(user);
        }
        return user;
    }


    /**
     * OAuth2 제공자의 사용자 정보 엔드포인트에서 사용자 속성을 가져옵니다.
     *
     * @param provider OAuth2 제공자의 정보
     * @param token 사용자의 OAuth2 토큰
     * @return 사용자의 속성을 담은 Map 객체
     */
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
