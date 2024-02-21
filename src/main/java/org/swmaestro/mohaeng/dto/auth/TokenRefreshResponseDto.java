package org.swmaestro.mohaeng.dto.auth;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class TokenRefreshResponseDto {

    private String accessToken;

    public static TokenRefreshResponseDto of(String accessToken) {
        TokenRefreshResponseDto tokenRefreshResponseDto = new TokenRefreshResponseDto();
        tokenRefreshResponseDto.accessToken = accessToken;
        return tokenRefreshResponseDto;
    }
}
