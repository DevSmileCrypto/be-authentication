package io.cryptobrewmaster.ms.be.authentication.web.controller;

import io.cryptobrewmaster.ms.be.authentication.service.authentication.AuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.service.authentication.keychain.HiveKeychainAuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.HiveSignerAuthenticationService;
import io.cryptobrewmaster.ms.be.authentication.web.model.AuthenticationTokenPairDto;
import io.cryptobrewmaster.ms.be.authentication.web.model.RegistrationOrLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Slf4j
@Valid
@RequiredArgsConstructor
@RequestMapping("/api/authentication")
@RestController
public class AuthenticationController {

    private final HiveKeychainAuthenticationService hiveKeychainAuthenticationService;
    private final HiveSignerAuthenticationService hiveSignerAuthenticationService;

    private final AuthenticationService authenticationService;

    @PostMapping("/login/keychain")
    public AuthenticationTokenPairDto registrationOrLoginByKeychain(@NotNull @RequestBody RegistrationOrLoginDto registrationOrLoginDto) {
        log.info("Request to registration or login account by keychain received. {}", registrationOrLoginDto);
        AuthenticationTokenPairDto authenticationTokenPairDto = hiveKeychainAuthenticationService.registrationOrLogin(registrationOrLoginDto);
        log.info("Response on registration or login account by keychain. {}", authenticationTokenPairDto);
        return authenticationTokenPairDto;
    }

    @GetMapping("/login/signer")
    public RedirectView generateRegistrationOrLoginUrlBySigner() {
        log.info("Request to generate oauth registration or login url account by signer received.");
        RedirectView redirectView = hiveSignerAuthenticationService.generateLoginUrl();
        log.info("Response on generate oauth registration or login url account by signer. {}", redirectView.getUrl());
        return redirectView;
    }

    @GetMapping("/login/signer/redirect")
    public RedirectView completeRegistrationOrLoginBySigner(@NotNull @RequestParam MultiValueMap<String, String> params) {
        log.info("Request to complete redirect oauth registration or login account by signer received.");
        RedirectView redirectView = hiveSignerAuthenticationService.completeRegistrationOrLogin(params);
        log.info("Response on complete redirect oauth registration or login account by signer. {}", redirectView.getUrl());
        return redirectView;
    }

    @PostMapping("/logout/{accountId}")
    public void logout(@NotNull @PathVariable String accountId) {
        log.info("Request to logout account received. Account id = {}", accountId);
        authenticationService.logout(accountId);
        log.info("Response on logout account. Account id = {}", accountId);
    }

}
