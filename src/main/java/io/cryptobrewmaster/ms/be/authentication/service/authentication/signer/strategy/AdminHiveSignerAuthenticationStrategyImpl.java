package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.communication.ui.uri.UiUriService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AdminPermissionRepository;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveSignerProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.time.Clock;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@Service
public class AdminHiveSignerAuthenticationStrategyImpl extends BaseHiveSignerAuthenticationStrategy {

    private final AdminPermissionRepository adminPermissionRepository;

    private final JwtService jwtService;
    private final UiUriService uiUriService;

    private final Clock utcClock;

    public AdminHiveSignerAuthenticationStrategyImpl(AccountCommunicationService accountCommunicationService,
                                                     AdminPermissionRepository adminPermissionRepository, JwtService jwtService,
                                                     UiUriService uiUriService, HiveSignerProperties hiveSignerProperties,
                                                     AccountAuthenticationRepository accountAuthenticationRepository,
                                                     ObjectMapper mapper, Clock utcClock) {
        super(accountCommunicationService, accountAuthenticationRepository, hiveSignerProperties, mapper);
        this.adminPermissionRepository = adminPermissionRepository;
        this.jwtService = jwtService;
        this.uiUriService = uiUriService;
        this.utcClock = utcClock;
    }

    @Override
    public Supplier<URI> getUriWithNotSuccessSupplier() {
        return uiUriService::getAdminDashboardUriWithNotSuccess;
    }

    @Override
    public Supplier<URI> getUriWithSuccessSupplier(JwtTokenPair jwtTokenPair) {
        return () -> uiUriService.getAdminDashboardUriWithSuccess(jwtTokenPair);
    }

    @Override
    public Supplier<URI> getUriWithErrorSupplier() {
        return uiUriService::getAdminDashboardUriWithError;
    }

    @Override
    public JwtTokenPair login(AccountDto accountDto, Map<String, String> state, AccountAuthentication accountAuthentication) {
        var adminPermissionOptional = adminPermissionRepository.findByWalletAndAllowedTrue(accountDto.getWallet());
        if (adminPermissionOptional.isEmpty()) {
            throw new ParametersAbsentOrInvalidException(
                    String.format("The account with wallet = %s does not have permission to sign in", accountDto.getWallet())
            );
        }
        var adminPermission = adminPermissionOptional.get();

        var jwtTokenPair = jwtService.generatePair(accountAuthentication);

        Set<Role> roles = accountAuthentication.getRoles();
        if (!roles.containsAll(adminPermission.getRoles())) {
            accountAuthentication.getRoles().addAll(adminPermission.getRoles());
        }

        accountAuthentication.updateTokenInfo(jwtTokenPair, getType());
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return jwtTokenPair;
    }

    @Override
    public JwtTokenPair registration(AccountDto accountDto, Map<String, String> state) {
        var adminPermissionOptional = adminPermissionRepository.findByWalletAndAllowedTrue(accountDto.getWallet());
        if (adminPermissionOptional.isEmpty()) {
            throw new ParametersAbsentOrInvalidException(
                    String.format("The account with wallet = %s does not have permission to sign up", accountDto.getWallet())
            );
        }
        var adminPermission = adminPermissionOptional.get();

        var jwtTokenPair = jwtService.generatePair(accountDto.getId(), adminPermission.getRoles());

        var accountAuthentication = AccountAuthentication.of(
                accountDto.getId(), getType(), jwtTokenPair, adminPermission.getRoles(), utcClock
        );
        accountAuthenticationRepository.save(accountAuthentication);

        return jwtTokenPair;
    }


    @Override
    public GatewayType getType() {
        return GatewayType.ADMIN;
    }
}
