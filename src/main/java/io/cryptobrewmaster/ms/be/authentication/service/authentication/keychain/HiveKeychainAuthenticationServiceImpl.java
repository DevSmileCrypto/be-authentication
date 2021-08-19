package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain;

import io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.strategy.HiveKeychainAuthenticationStrategy;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HiveKeychainAuthenticationServiceImpl implements HiveKeychainAuthenticationService {

    private final List<HiveKeychainAuthenticationStrategy> hiveKeychainAuthenticationStrategies;

    @Override
    public Mono<AuthenticationTokenPairDto> registrationOrLogin(RegistrationOrLoginDto registrationOrLoginDto, GatewayType type) {
        return hiveKeychainAuthenticationStrategies.stream()
                .filter(strategy -> strategy.getType() == type)
                .findFirst()
                .orElseThrow(() -> new InnerServiceException(
                        String.format("Hive keychain authentication strategy with type = %s not exists in system", type)
                ))
                .registrationOrLogin(registrationOrLoginDto);
    }

}
