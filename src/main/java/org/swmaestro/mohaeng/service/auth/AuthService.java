package org.swmaestro.mohaeng.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.swmaestro.mohaeng.util.jwt.JwtUtil;
import org.swmaestro.mohaeng.dto.auth.TokenRefreshResponseDto;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final JwtUtil jwtUtil;

    /**
     * Reissue Token
     * @param  accessToken String
     * @param  refreshToken String
     * @return TokenRefreshResponseDto
     */
    public TokenRefreshResponseDto reissueToken(String accessToken, String refreshToken) {
        return TokenRefreshResponseDto.of(jwtUtil.reissueAccessToken(accessToken, refreshToken));
    }
}
