package org.swmaestro.mohaeng.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class TokenRequest {

    @NotBlank(message = "Refresh Token을 입력해주세요.")
    String refreshToken;
}
