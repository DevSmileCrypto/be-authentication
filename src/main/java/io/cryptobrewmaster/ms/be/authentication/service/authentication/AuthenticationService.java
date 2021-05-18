package io.cryptobrewmaster.ms.be.authentication.service.authentication;

import io.cryptobrewmaster.ms.be.library.constants.GatewayType;

/**
 * The interface Authentication service.
 */
public interface AuthenticationService {

    /**
     * Logout.
     *
     * @param accountId the account id
     * @param type      the type
     */
    void logout(String accountId, GatewayType type);

}
