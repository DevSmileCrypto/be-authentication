package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.communication.ui.uri.UiUriService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.kafka.account.AccountKafkaSender;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveSignerProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Clock;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class ApiHiveSignerAuthenticationStrategyImpl extends BaseHiveSignerAuthenticationStrategy {

    private final AccountKafkaSender accountKafkaSender;

    private final JwtService jwtService;
    private final UiUriService uiUriService;

    @Value("${authentication.default.roles:USER}")
    private Set<Role> defaultAuthenticationRoles;

    private final Clock utcClock;

    public ApiHiveSignerAuthenticationStrategyImpl(AccountCommunicationService accountCommunicationService,
                                                   AccountKafkaSender accountKafkaSender, JwtService jwtService,
                                                   UiUriService uiUriService, HiveSignerProperties hiveSignerProperties,
                                                   AccountAuthenticationRepository accountAuthenticationRepository,
                                                   ObjectMapper mapper, Clock utcClock) {
        super(accountCommunicationService, accountAuthenticationRepository, hiveSignerProperties, mapper);
        this.accountKafkaSender = accountKafkaSender;
        this.jwtService = jwtService;
        this.uiUriService = uiUriService;
        this.utcClock = utcClock;
    }

    @Override
    public Supplier<URI> getUriWithNotSuccessSupplier() {
        return uiUriService::getDashboardUriWithNotSuccess;
    }

    @Override
    public Supplier<URI> getUriWithSuccessSupplier(JwtTokenPair jwtTokenPair) {
        return () -> uiUriService.getDashboardUriWithSuccess(jwtTokenPair);
    }

    @Override
    public Supplier<URI> getUriWithErrorSupplier() {
        return uiUriService::getDashboardUriWithError;
    }

    @Override
    public JwtTokenPair login(AccountDto accountDto, Map<String, String> state, AccountAuthentication accountAuthentication) {
        var jwtTokenPair = jwtService.generatePair(accountAuthentication);

        if (!accountDto.isInitialized()) {
            accountKafkaSender.init(accountDto);

            accountCommunicationService.initialize(accountDto.getId());

            accountAuthentication.getRoles().addAll(defaultAuthenticationRoles);
        }

        accountAuthentication.updateTokenInfo(jwtTokenPair, getType());
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return jwtTokenPair;
    }

    @Override
    public JwtTokenPair registration(AccountDto accountDto, Map<String, String> state) {
        var jwtTokenPair = jwtService.generatePair(accountDto.getId(), defaultAuthenticationRoles);

        var accountAuthentication = AccountAuthentication.of(
                accountDto.getId(), getType(), jwtTokenPair, defaultAuthenticationRoles, utcClock
        );
        accountAuthenticationRepository.save(accountAuthentication);

        accountKafkaSender.init(accountDto);

        accountCommunicationService.initialize(accountDto.getId());

        return jwtTokenPair;
    }


    @Override
    public GatewayType getType() {
        return GatewayType.API;
    }
}
