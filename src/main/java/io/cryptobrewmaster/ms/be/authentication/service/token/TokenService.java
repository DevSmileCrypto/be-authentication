package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import reactor.core.publisher.Mono;

/**
 * The interface Token service.
 */
public interface TokenService {

    /**
     * Validate mono.
     *
     * @param accessToken the access token
     * @param type        the type
     * @return the mono
     */
    Mono<AccountAuthenticationDto> validate(String accessToken, GatewayType type);

    /**
     * Refresh mono.
     *
     * @param refreshToken the refresh token
     * @param type         the type
     * @return the mono
     */
    Mono<AuthenticationTokenPairDto> refresh(String refreshToken, GatewayType type);

}
