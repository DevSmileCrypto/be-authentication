package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.strategy;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.kafka.account.AccountKafkaSender;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveKeychainProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.util.Set;

@Service
public class ApiHiveKeychainAuthenticationStrategyImpl extends BaseHiveKeychainAuthenticationStrategy {

    private final AccountKafkaSender accountKafkaSender;

    private final JwtService jwtService;

    private final Clock utcClock;

    @Value("${authentication.default.roles:USER}")
    private Set<Role> defaultAuthenticationRoles;

    public ApiHiveKeychainAuthenticationStrategyImpl(AccountCommunicationService accountCommunicationService,
                                                     AccountKafkaSender accountKafkaSender, JwtService jwtService,
                                                     AccountAuthenticationRepository accountAuthenticationRepository,
                                                     HiveKeychainProperties hiveKeychainProperties,
                                                     Clock utcClock) {
        super(accountCommunicationService, accountAuthenticationRepository, hiveKeychainProperties);
        this.accountKafkaSender = accountKafkaSender;
        this.jwtService = jwtService;
        this.utcClock = utcClock;
    }

    @Override
    public Mono<AuthenticationTokenPairDto> login(AccountDto accountDto, AccountAuthentication accountAuthentication) {
        var jwtTokenPair = jwtService.generatePair(accountAuthentication);

        if (!accountDto.isInitialized()) {
            accountKafkaSender.init(accountDto);
            accountAuthentication.getRoles().addAll(defaultAuthenticationRoles);
        }

        accountAuthentication.updateTokenInfo(jwtTokenPair, getType());
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        return accountAuthenticationRepository.save(accountAuthentication)
                .map(updatedAccountAuthentication -> updatedAccountAuthentication.getTokenInfo(getType()))
                .map(AuthenticationTokenPairDto::of);
    }

    @Override
    public Mono<AuthenticationTokenPairDto> registration(AccountDto accountDto) {
        var jwtTokenPair = jwtService.generatePair(accountDto.getId(), defaultAuthenticationRoles);

        var accountAuthentication = AccountAuthentication.of(
                accountDto.getId(), getType(), jwtTokenPair, defaultAuthenticationRoles, utcClock
        );
        return accountAuthenticationRepository.save(accountAuthentication)
                .map(savedAccountAuthentication -> {
                    accountKafkaSender.init(accountDto);

                    return savedAccountAuthentication.getTokenInfo(getType());
                })
                .map(AuthenticationTokenPairDto::of);
    }

    @Override
    public GatewayType getType() {
        return GatewayType.API;
    }
}
