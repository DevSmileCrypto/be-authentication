package io.cryptobrewmaster.ms.be.authentication.service.jwt;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.library.constants.Role;

import java.util.List;

/**
 * The interface Jwt service.
 */
public interface JwtService {

    /**
     * Generate pair jwt token pair.
     *
     * @param accountId the account id
     * @param roles     the roles
     * @return the jwt token pair
     */
    JwtTokenPair generatePair(String accountId, List<Role> roles);

    /**
     * Generate pair jwt token pair.
     *
     * @param accountAuthentication the account authentication
     * @return the jwt token pair
     */
    JwtTokenPair generatePair(AccountAuthentication accountAuthentication);

    /**
     * Validate boolean.
     *
     * @param token the token
     * @return the boolean
     */
    boolean validate(String token);

    /**
     * Gets account id from refresh token.
     *
     * @param refreshToken the refresh token
     * @return the account id from refresh token
     */
    String getUidFromRefreshToken(String refreshToken);

    /**
     * Gets account id from access token.
     *
     * @param accessToken the access token
     * @return the account id from access token
     */
    String getUidFromAccessToken(String accessToken);

}
