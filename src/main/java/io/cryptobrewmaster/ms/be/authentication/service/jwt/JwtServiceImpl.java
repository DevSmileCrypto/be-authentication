package io.cryptobrewmaster.ms.be.authentication.service.jwt;

import io.cryptobrewmaster.ms.be.authentication.constant.Role;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.model.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.JwtProperties;
import io.cryptobrewmaster.ms.be.authentication.util.JwtUtil;
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
    public JwtTokenPair generatePair(String accountId, List<Role> roles) {
        long now = utcClock.millis();

        long expirationAccessTokenDate = now + jwtProperties.getToken().getAccess().getLongExpireLength();
        long expirationRefreshTokenDate = now + jwtProperties.getToken().getRefresh().getLongExpireLength();

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
    public JwtTokenPair generatePair(AccountAuthentication accountAuthentication) {
        long now = utcClock.millis();

        long expirationAccessTokenDate = now + jwtProperties.getToken().getAccess().getLongExpireLength();
        long expirationRefreshTokenDate = now + jwtProperties.getToken().getRefresh().getLongExpireLength();

        Date nowInDate = new Date(now);
        String encodedTokenSecret = encodeTokenSecret();
        String encodedCryptoSecret = encodeCryptoSecret();

        Map<String, Object> claims = Map.of("roles", accountAuthentication.getRoles());

        String accessToken = JwtUtil.generate(
                accountAuthentication.getAccountId(), claims, nowInDate, new Date(expirationAccessTokenDate),
                encodedTokenSecret, encodedCryptoSecret
        );
        String refreshToken = JwtUtil.generate(
                accountAuthentication.getAccountId(), claims, nowInDate, new Date(expirationRefreshTokenDate),
                encodedTokenSecret, encodedCryptoSecret
        );

        return new JwtTokenPair(
                accessToken, expirationAccessTokenDate,
                refreshToken, expirationRefreshTokenDate
        );
    }

    private String encodeCryptoSecret() {
        return Base64.getEncoder().encodeToString(jwtProperties.getCrypto().getSecretKey().getBytes(UTF_8));
    }

    private String encodeTokenSecret() {
        return Base64.getEncoder().encodeToString(jwtProperties.getToken().getSecretKey().getBytes(UTF_8));
    }

}
