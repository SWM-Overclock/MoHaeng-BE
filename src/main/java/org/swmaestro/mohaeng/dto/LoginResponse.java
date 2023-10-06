package org.swmaestro.mohaeng.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginResponse {
    private Long id;
    private String nickName;
    private String email;
    private String imageUrl;
    private String tokenType;
    private String accessToken;
    private String refreshToken;

    @Builder
    public LoginResponse(Long id, String nickName, String email, String imageUrl, String tokenType, String accessToken, String refreshToken) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}