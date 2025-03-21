package org.zerock.arcteryx.provider.jwtToken;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JwtTokenDTO {
    private String accessToken;
    private String refreshToken;
}
