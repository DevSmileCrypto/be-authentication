package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import org.springframework.util.MultiValueMap;

/**
 * The interface Hive signer authentication service.
 */
public interface HiveSignerAuthenticationService {

    /**
     * Generate login url string.
     *
     * @return the string
     */
    String generateLoginUrl();

    /**
     * Complete registration or login string.
     *
     * @param params the params
     * @return the string
     */
    String completeRegistrationOrLogin(MultiValueMap<String, String> params);

}
