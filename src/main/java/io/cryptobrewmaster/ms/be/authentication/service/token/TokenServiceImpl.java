package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final Clock utcClock;

    @Override
    public AccountAuthenticationDto validate(String accessToken) {
        boolean valid = jwtService.validate(accessToken);
        if (!valid) {
            return AccountAuthenticationDto.of(false);
        }
        String accountId = jwtService.getAccountIdFromAccessToken(accessToken);
        AccountAuthentication accountAuthentication = accountAuthenticationRepository.getByAccountIdAndAccessToken(
                accountId, accessToken
        );
        return AccountAuthenticationDto.of(true, accountAuthentication);
    }

    @Override
    public AuthenticationTokenPairDto refresh(String refreshToken) {
        String accountId = jwtService.getAccountIdFromRefreshToken(refreshToken);
        AccountAuthentication accountAuthentication = accountAuthenticationRepository.getByAccountIdAndRefreshToken(
                accountId, refreshToken
        );

        JwtTokenPair jwtTokenPair = jwtService.generatePair(accountAuthentication);

        accountAuthentication.updateTokenPair(jwtTokenPair);
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

}
