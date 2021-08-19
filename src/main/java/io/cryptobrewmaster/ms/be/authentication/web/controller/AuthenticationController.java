package io.cryptobrewmaster.ms.be.authentication.web.controller;

import io.cryptobrewmaster.ms.be.authentication.service.authentication.AuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.HiveKeychainAuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.HiveSignerAuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {

    private final HiveKeychainAuthenticationService hiveKeychainAuthenticationService;
    private final HiveSignerAuthenticationService hiveSignerAuthenticationService;

    private final AuthenticationService authenticationService;

    @PostMapping("/login/keychain")
    public Mono<AuthenticationTokenPairDto> registrationOrLoginByKeychain(@Valid @NotNull @RequestBody RegistrationOrLoginDto registrationOrLoginDto,
                                                                          @Valid @NotNull @RequestParam GatewayType type) {
        return hiveKeychainAuthenticationService.registrationOrLogin(registrationOrLoginDto, type);
    }

    @GetMapping("/login/signer")
    public Mono<String> generateRegistrationOrLoginUrlBySigner(@Valid @NotNull @RequestParam GatewayType type) {
        return hiveSignerAuthenticationService.generateLoginUrl(type);
    }

    @GetMapping("/login/signer/redirect")
    public Mono<String> completeRegistrationOrLoginBySigner(@Valid @NotNull @RequestParam MultiValueMap<String, String> params,
                                                            @Valid @NotNull @RequestParam GatewayType type) {
        return hiveSignerAuthenticationService.completeRegistrationOrLogin(params, type);
    }

    @PutMapping("/logout/{accountId}")
    public void logout(@Valid @NotBlank @PathVariable String accountId,
                             @Valid @NotNull @RequestParam GatewayType type) {
         authenticationService.logout(accountId, type);
    }

}
