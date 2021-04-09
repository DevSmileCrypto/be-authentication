package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;

/**
 * The interface Token service.
 */
public interface TokenService {

    /**
     * Validate account authentication dto.
     *
     * @param accessToken the access token
     * @return the account authentication dto
     */
    AccountAuthenticationDto validate(String accessToken);

    /**
     * Refresh authentication token pair dto.
     *
     * @param refreshToken the refresh token
     * @return the authentication token pair dto
     */
    AuthenticationTokenPairDto refresh(String refreshToken);

}
