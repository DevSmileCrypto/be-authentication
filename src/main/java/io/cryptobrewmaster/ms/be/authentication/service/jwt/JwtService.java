package io.cryptobrewmaster.ms.be.authentication.service.jwt;

import io.cryptobrewmaster.ms.be.authentication.constant.Role;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.model.JwtTokenPair;

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

}
