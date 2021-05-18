package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;

/**
 * The interface Token service.
 */
public interface TokenService {

    /**
     * Validate account authentication dto.
     *
     * @param accessToken the access token
     * @param type        the type
     * @return the account authentication dto
     */
    AccountAuthenticationDto validate(String accessToken, GatewayType type);

    /**
     * Refresh authentication token pair dto.
     *
     * @param refreshToken the refresh token
     * @param type         the type
     * @return the authentication token pair dto
     */
    AuthenticationTokenPairDto refresh(String refreshToken, GatewayType type);

}
