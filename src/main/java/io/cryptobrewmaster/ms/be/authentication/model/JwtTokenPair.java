package io.cryptobrewmaster.ms.be.authentication.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtTokenPair {
    @NotBlank
    private String accessToken;
    @NotNull
    private Long expirationAccessTokenDate;
    @NotBlank
    private String refreshToken;
    @NotNull
    private Long expirationRefreshTokenDate;
}