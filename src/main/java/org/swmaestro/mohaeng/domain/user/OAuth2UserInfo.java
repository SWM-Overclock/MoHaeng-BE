package org.swmaestro.mohaeng.domain.user;

public interface OAuth2UserInfo {
    String getProviderId();

    String getProvider();

    String getEmail();

    String getNickName();

    String getImageUrl();
}
