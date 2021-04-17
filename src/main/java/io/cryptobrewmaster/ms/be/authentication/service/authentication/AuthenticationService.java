package io.cryptobrewmaster.ms.be.authentication.service.authentication;

/**
 * The interface Authentication service.
 */
public interface AuthenticationService {

    /**
     * Logout.
     *
     * @param uid the uid
     */
    void logout(String uid);

}
