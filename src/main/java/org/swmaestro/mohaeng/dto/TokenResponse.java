package org.swmaestro.mohaeng.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TokenResponse {

    private String accessToken;

    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
