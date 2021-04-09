package io.cryptobrewmaster.ms.be.authentication.util;

import io.cryptobrewmaster.ms.be.authentication.exception.InnerServiceException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.util.Date;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtil {

    public static String generate(String accountId, Map<String, Object> claims, Date now,
                                  Date validity, String tokenSecretKey, String cryptoSecretKey) {

        return Jwts.builder()
                .setSubject(EnDeCrypter.encrypt(accountId, cryptoSecretKey))
                .setIssuedAt(now)
                .setExpiration(validity)
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, tokenSecretKey)
                .compact();
    }

    public static boolean validate(String token, String tokenSecretKey, Clock utcClock) {
        try {
            Date now = new Date(utcClock.millis());

            return !Jwts.parser()
                    .setSigningKey(tokenSecretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(now);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InnerServiceException(
                    String.format("The JWT token has not been validated. Token = %s", token)
            );
        }
    }

}
