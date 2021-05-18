package io.cryptobrewmaster.ms.be.authentication.service.authentication;

import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final Clock utcClock;

    @Override
    public void logout(String accountId, GatewayType type) {
        var accountAuthentication = accountAuthenticationRepository.getByAccountId(accountId);
        accountAuthentication.clearTokenInfo(type);
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);
    }

}
