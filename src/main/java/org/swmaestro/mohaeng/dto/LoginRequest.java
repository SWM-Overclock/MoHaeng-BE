package org.swmaestro.mohaeng.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LoginRequest {
    private String code;

    public LoginRequest(String code) {
        this.code = code;
    }
}
