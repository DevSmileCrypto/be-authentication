package io.cryptobrewmaster.ms.be.authentication.service.token;

import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtService jwtService;

    @Override
    public AccountAuthenticationDto validate(String accessToken) {
        return null;
    }

    @Override
    public AuthenticationTokenPairDto refresh(String refreshToken) {
        return null;
    }

}
