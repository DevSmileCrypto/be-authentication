package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain;

import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import reactor.core.publisher.Mono;

/**
 * The interface Hive keychain authentication service.
 */
public interface HiveKeychainAuthenticationService {

    /**
     * Registration or login mono.
     *
     * @param registrationOrLoginDto the registration or login dto
     * @param type                   the type
     * @return the mono
     */
    Mono<AuthenticationTokenPairDto> registrationOrLogin(RegistrationOrLoginDto registrationOrLoginDto, GatewayType type);

}
