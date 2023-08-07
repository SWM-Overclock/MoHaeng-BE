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


    /**
     * @InMemoryRepository application-oauth properties 정보를 담고 있음
     * @getToken() 넘겨받은 code를 이용하여 Oauth 서버에 Token 요청
     * @getUserProfile Oauth 서버에 Token을 이용하여 User 정보 요청
     * @return LoginResponse
     */
    @Transactional
    public LoginResponse login(String provider, String code) {

        log.info("in OAuthService");
        ClientRegistration clientRegistration = inMemoryRepository.findByRegistrationId(provider);
        OAuthTokenResponse tokenResponse = getToken(clientRegistration, code);
        User user = getUserProfile(provider, tokenResponse, clientRegistration);

        return LoginResponse.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .build();
    }

    private OAuthTokenResponse getToken(ClientRegistration provider, String code) {
        log.info("OauthService.getToken In");
        log.info("provider.TokenUri = {}" , provider.getProviderDetails().getTokenUri());
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                    log.info("header = {}", header);
                })
                .bodyValue(tokenRequest(code, provider))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> tokenRequest(String code, ClientRegistration provider) {
        log.info("tokenRequest In");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret",provider.getClientSecret());
        formData.add("client_id",provider.getClientId());
        log.info("redirectUri = {}", provider.getRedirectUri());
        return formData;
    }

    private User getUserProfile(String provider, OAuthTokenResponse tokenResponse, ClientRegistration clientRegistration) {
        Map<String, Object> userAttributes = getUserAttributes(clientRegistration, tokenResponse);
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

    private Map<String, Object> getUserAttributes(ClientRegistration provider, OAuthTokenResponse tokenResponse) {
        log.info("getUserAttributes In");
        log.info("userinfoUri = {}", provider.getProviderDetails().getUserInfoEndpoint().getUri());
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(tokenResponse.getAccessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }
}
