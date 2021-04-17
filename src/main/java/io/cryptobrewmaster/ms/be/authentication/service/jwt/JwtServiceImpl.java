package io.cryptobrewmaster.ms.be.authentication.service.jwt;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.jwt.JwtProperties;
import io.cryptobrewmaster.ms.be.authentication.util.jwt.JwtUtil;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import io.cryptobrewmaster.ms.be.library.exception.authentication.InvalidAccessTokenException;
import io.cryptobrewmaster.ms.be.library.exception.authentication.InvalidRefreshTokenException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RequiredArgsConstructor
@Service
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;

    private final Clock utcClock;

    @Override
    public JwtTokenPair generatePair(AccountAuthentication accountAuthentication) {
        return generatePair(accountAuthentication.getAccountId(), accountAuthentication.getRoles());
    }

    @Override
    public boolean validate(String token) {
        return JwtUtil.validate(token, encodeTokenSecret(), utcClock);
    }

    @Override
    public JwtTokenPair generatePair(String accountId, List<Role> roles) {
        long now = utcClock.millis();
        JwtProperties.Token tokenProperties = jwtProperties.getToken();

        long expirationAccessTokenDate = now + tokenProperties.getAccess().getLongExpireLength();
        long expirationRefreshTokenDate = now + tokenProperties.getRefresh().getLongExpireLength();

        Date nowInDate = new Date(now);
        String encodedTokenSecret = encodeTokenSecret();
        String encodedCryptoSecret = encodeCryptoSecret();

        Map<String, Object> claims = Map.of("roles", roles);

        String accessToken = JwtUtil.generate(
                accountId, claims, nowInDate, new Date(expirationAccessTokenDate),
                encodedTokenSecret, encodedCryptoSecret
        );
        String refreshToken = JwtUtil.generate(
                accountId, claims, nowInDate, new Date(expirationRefreshTokenDate),
                encodedTokenSecret, encodedCryptoSecret
        );

        return new JwtTokenPair(
                accessToken, expirationAccessTokenDate,
                refreshToken, expirationRefreshTokenDate
        );
    }

    @Override
    public String getAccountIdFromRefreshToken(String refreshToken) {
        try {
            return JwtUtil.getUid(refreshToken, encodeTokenSecret(), encodeCryptoSecret());
        } catch (ExpiredJwtException e) {
            throw new InvalidRefreshTokenException(
                    String.format("Expiration exhausted of the refresh token = %s. Error = %s", refreshToken, e.getMessage())
            );
        } catch (Exception e) {
            throw new InvalidRefreshTokenException(
                    String.format("Some errors with refresh token = %s. Error = %s", refreshToken, e.getMessage())
            );
        }
    }

    @Override
    public String getAccountIdFromAccessToken(String accessToken) {
        try {
            return JwtUtil.getUid(accessToken, encodeTokenSecret(), encodeCryptoSecret());
        } catch (ExpiredJwtException e) {
            throw new InvalidAccessTokenException(
                    String.format("Expiration exhausted of the access token = %s. Error = %s", accessToken, e.getMessage())
            );
        } catch (Exception e) {
            throw new InvalidAccessTokenException(
                    String.format("Some errors with access token = %s. Error = %s", accessToken, e.getMessage())
            );
        }
    }

    private String encodeCryptoSecret() {
        String secretKey = jwtProperties.getCrypto().getSecretKey();
        return Base64.getEncoder()
                .encodeToString(secretKey.getBytes(UTF_8));
    }

    private String encodeTokenSecret() {
        String secretKey = jwtProperties.getToken().getSecretKey();
        return Base64.getEncoder()
                .encodeToString(secretKey.getBytes(UTF_8));
    }

}
