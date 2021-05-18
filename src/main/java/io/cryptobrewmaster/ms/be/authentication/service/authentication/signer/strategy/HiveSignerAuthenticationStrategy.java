package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.strategy;

import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import org.springframework.util.MultiValueMap;

/**
 * The interface Hive signer authentication strategy.
 */
public interface HiveSignerAuthenticationStrategy {

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

    /**
     * Gets type.
     *
     * @return the type
     */
    GatewayType getType();

}
