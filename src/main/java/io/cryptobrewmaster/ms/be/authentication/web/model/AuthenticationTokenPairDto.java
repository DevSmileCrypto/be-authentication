package io.cryptobrewmaster.ms.be.authentication.web.model;

import io.cryptobrewmaster.ms.be.authentication.model.JwtTokenPair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationTokenPairDto {
    @NotBlank
    private String accessToken;
    @NotNull
    private Long expirationAccessTokenDate;
    @NotBlank
    private String refreshToken;
    @NotNull
    private Long expirationRefreshTokenDate;

    public static AuthenticationTokenPairDto of(JwtTokenPair jwtTokenPair) {
        return new AuthenticationTokenPairDto(
                jwtTokenPair.getAccessToken(), jwtTokenPair.getExpirationAccessTokenDate(),
                jwtTokenPair.getRefreshToken(), jwtTokenPair.getExpirationRefreshTokenDate()
        );
    }
}
