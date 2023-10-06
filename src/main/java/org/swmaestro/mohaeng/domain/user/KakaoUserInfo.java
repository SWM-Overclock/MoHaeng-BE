package org.swmaestro.mohaeng.domain.user;

import lombok.Getter;

import java.util.Map;


@Getter
public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes;

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String) getKakaoAccount().get("email");
    }

    @Override
    public String getNickName() {
        return (String) getProfile().get("nickname");
    }

    @Override
    public String getImageUrl() {
        return (String)getProfile().get("profile_image_url");
    }

    public Map<String, Object> getKakaoAccount(){
        return(Map<String, Object>) attributes.get("kakao_account");
    }

    public Map<String, Object> getProfile(){
        return (Map<String, Object>) getKakaoAccount().get("profile");
    }
}
