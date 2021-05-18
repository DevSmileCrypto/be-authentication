package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.strategy;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AdminPermissionRepository;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveKeychainProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Set;

@Service
public class AdminHiveKeychainAuthenticationStrategyImpl extends BaseHiveKeychainAuthenticationStrategy {

    private final AdminPermissionRepository adminPermissionRepository;

    private final JwtService jwtService;

    private final Clock utcClock;

    public AdminHiveKeychainAuthenticationStrategyImpl(AccountCommunicationService accountCommunicationService,
                                                       AccountAuthenticationRepository accountAuthenticationRepository,
                                                       AdminPermissionRepository adminPermissionRepository,
                                                       HiveKeychainProperties hiveKeychainProperties, JwtService jwtService,
                                                       Clock utcClock) {
        super(accountCommunicationService, accountAuthenticationRepository, hiveKeychainProperties);
        this.adminPermissionRepository = adminPermissionRepository;
        this.jwtService = jwtService;
        this.utcClock = utcClock;
    }

    @Override
    public AuthenticationTokenPairDto login(AccountDto accountDto, AccountAuthentication accountAuthentication) {
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

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

    @Override
    public AuthenticationTokenPairDto registration(AccountDto accountDto) {
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

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

    @Override
    public GatewayType getType() {
        return GatewayType.ADMIN;
    }
}
