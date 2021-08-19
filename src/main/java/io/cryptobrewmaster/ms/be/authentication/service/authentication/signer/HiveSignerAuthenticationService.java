package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

/**
 * The interface Hive signer authentication service.
 */
public interface HiveSignerAuthenticationService {

    /**
     * Generate login url mono.
     *
     * @param type the type
     * @return the mono
     */
    Mono<String> generateLoginUrl(GatewayType type);

    /**
     * Complete registration or login mono.
     *
     * @param params the params
     * @param type   the type
     * @return the mono
     */
    Mono<String> completeRegistrationOrLogin(MultiValueMap<String, String> params, GatewayType type);

}
