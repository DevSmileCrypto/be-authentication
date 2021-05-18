package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import org.springframework.util.MultiValueMap;

/**
 * The interface Hive signer authentication service.
 */
public interface HiveSignerAuthenticationService {

    /**
     * Generate login url string.
     *
     * @param type the type
     * @return the string
     */
    String generateLoginUrl(GatewayType type);

    /**
     * Complete registration or login string.
     *
     * @param params the params
     * @param type   the type
     * @return the string
     */
    String completeRegistrationOrLogin(MultiValueMap<String, String> params, GatewayType type);

}
