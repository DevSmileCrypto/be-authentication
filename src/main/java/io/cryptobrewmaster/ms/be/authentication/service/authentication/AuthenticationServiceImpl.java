package io.cryptobrewmaster.ms.be.authentication.service.authentication;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final Clock utcClock;

    @Override
    public void logout(String uid) {
        AccountAuthentication accountAuthentication = accountAuthenticationRepository.getByUid(uid);
        accountAuthentication.clearTokenPair();
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);
    }

}
