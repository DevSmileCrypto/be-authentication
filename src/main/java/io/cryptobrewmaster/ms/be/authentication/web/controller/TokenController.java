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
import reactor.core.publisher.Mono;

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
    public Mono<AccountAuthenticationDto> validate(@Valid @NotBlank @PathVariable String accessToken,
                                                   @Valid @NotNull @RequestParam GatewayType type) {
        return tokenService.validate(accessToken, type);
    }

    @PutMapping("/refresh/{refreshToken}")
    public Mono<AuthenticationTokenPairDto> refresh(@Valid @NotBlank @PathVariable String refreshToken,
                                                    @Valid @NotNull @RequestParam GatewayType type) {
        return tokenService.refresh(refreshToken, type);
    }

}
