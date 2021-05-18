package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.exception.authentication.InvalidRefreshTokenException;
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
    public AccountAuthenticationDto validate(String accessToken, GatewayType type) {
        boolean valid = jwtService.validate(accessToken);
        if (!valid) {
            return AccountAuthenticationDto.of(false);
        }
        String accountId = jwtService.getAccountIdFromAccessToken(accessToken);
        var accountAuthentication = accountAuthenticationRepository.getByAccountId(accountId);

        var tokenInfo = accountAuthentication.getTokenInfo(type);
        if (!tokenInfo.getAccessToken().equals(accessToken)) {
            return AccountAuthenticationDto.of(false);
        }
        return AccountAuthenticationDto.of(true, accountAuthentication);
    }

    @Override
    public AuthenticationTokenPairDto refresh(String refreshToken, GatewayType type) {
        String accountId = jwtService.getAccountIdFromRefreshToken(refreshToken);
        var accountAuthentication = accountAuthenticationRepository.getByAccountId(accountId);

        var tokenInfo = accountAuthentication.getTokenInfo(type);
        if (!tokenInfo.getRefreshToken().equals(refreshToken)) {
            throw new InvalidRefreshTokenException(
                    String.format("Incorrect refresh token received for account with account id = %s", accountId)
            );
        }

        var jwtTokenPair = jwtService.generatePair(accountAuthentication);

        accountAuthentication.updateTokenInfo(jwtTokenPair, type);
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return AuthenticationTokenPairDto.of(jwtTokenPair);
    }

}
