package io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain;

import io.cryptobrewmaster.ms.be.authentication.constant.Role;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.integration.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.integration.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.model.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HiveKeychainAuthenticationServiceImpl implements HiveKeychainAuthenticationService {

    private final AccountCommunicationService accountCommunicationService;
    private final JwtService jwtService;

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    @Value("${authentication.default.roles:USER}")
    private List<Role> defaultAuthenticationRoles;

    private final Clock utcClock;

    @Override
    public AuthenticationTokenPairDto registrationOrLogin(RegistrationOrLoginDto registrationOrLoginDto) {
        AccountDto accountDto = accountCommunicationService.createOrGet(registrationOrLoginDto.getWallet());

        var accountAuthenticationOptional = accountAuthenticationRepository.findById(accountDto.getAccountId());
        if (accountAuthenticationOptional.isEmpty()) {
            JwtTokenPair jwtTokenPair = jwtService.generatePair(accountDto.getAccountId(), defaultAuthenticationRoles);

            AccountAuthentication accountAuthentication = AccountAuthentication.of(
                    accountDto.getAccountId(), jwtTokenPair, defaultAuthenticationRoles, utcClock
            );
            accountAuthenticationRepository.save(accountAuthentication);

            return AuthenticationTokenPairDto.of(jwtTokenPair);
        }

        AccountAuthentication accountAuthentication = accountAuthenticationOptional.get();
        JwtTokenPair jwtTokenPair = jwtService.generatePair(accountAuthentication);

        accountAuthentication.updateTokenPair(jwtTokenPair);
        accountAuthentication.setLastModifiedDate(utcClock.millis());

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

}
