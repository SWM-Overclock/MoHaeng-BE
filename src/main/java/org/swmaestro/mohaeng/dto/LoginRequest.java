package org.swmaestro.mohaeng.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LoginRequest {

    @NotBlank(message = "토큰을 입력해주세요.")
    private String token;
}
