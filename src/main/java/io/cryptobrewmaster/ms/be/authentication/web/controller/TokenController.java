package io.cryptobrewmaster.ms.be.authentication.web.controller;

import io.cryptobrewmaster.ms.be.authentication.service.token.TokenService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AccountAuthenticationDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/authentication/token")
@RestController
public class TokenController {

    private final TokenService tokenService;

    @GetMapping("/validate/{accessToken}")
    public AccountAuthenticationDto validate(@Valid @NotBlank @PathVariable String accessToken,
                                             @Valid @NotNull @RequestParam GatewayType type) {
        log.info("Request to validate access token received. Access token = {}, type = {}", accessToken, type);
        var authTokenPairDto = tokenService.validate(accessToken, type);
        log.info("Response on validate access token. {}", authTokenPairDto);
        return authTokenPairDto;
    }

    @PutMapping("/refresh/{refreshToken}")
    public AuthenticationTokenPairDto refresh(@Valid @NotBlank @PathVariable String refreshToken,
                                              @Valid @NotNull @RequestParam GatewayType type) {
        log.info("Request to refresh auth token pair received. Refresh token = {}, type = {}", refreshToken, type);
        var authenticationTokenPairDto = tokenService.refresh(refreshToken, type);
        log.info("Response on refresh auth token pair. {}", authenticationTokenPairDto);
        return authenticationTokenPairDto;
    }

}
