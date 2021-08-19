package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.strategy;

import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import reactor.core.publisher.Mono;

/**
 * The interface Hive keychain authentication strategy.
 */
public interface HiveKeychainAuthenticationStrategy {

    /**
     * Registration or login mono.
     *
     * @param registrationOrLoginDto the registration or login dto
     * @return the mono
     */
    Mono<AuthenticationTokenPairDto> registrationOrLogin(RegistrationOrLoginDto registrationOrLoginDto);

    /**
     * Gets type.
     *
     * @return the type
     */
    GatewayType getType();

}
