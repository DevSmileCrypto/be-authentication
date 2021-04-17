package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectOptions;
import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.communication.ui.uri.UiUriService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.kafka.account.AccountKafkaSender;
import io.cryptobrewmaster.ms.be.authentication.model.hive.signer.HiveSignerAccountData;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveSignerProperties;
import io.cryptobrewmaster.ms.be.authentication.service.jwt.JwtService;
import io.cryptobrewmaster.ms.be.authentication.util.hive.signer.HiveSignerUtil;
import io.cryptobrewmaster.ms.be.library.constants.Role;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.time.Clock;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveSignerAuthenticationServiceImpl implements HiveSignerAuthenticationService {

    private final AccountCommunicationService accountCommunicationService;
    private final AccountKafkaSender accountKafkaSender;

    private final JwtService jwtService;
    private final UiUriService uiUriService;

    private final AccountAuthenticationRepository accountAuthenticationRepository;

    private final HiveSignerProperties hiveSignerProperties;

    @Value("${authentication.default.roles:USER}")
    private List<Role> defaultAuthenticationRoles;

    private final ObjectMapper mapper;
    private final Clock utcClock;

    @Override
    public String generateLoginUrl() {
        try {
            return new SteemConnect(hiveSignerProperties.getSteemConnectOptions()).getLoginUrl(true);
        } catch (Exception e) {
            throw new InnerServiceException(
                    String.format("Error while on generate hive signer login url. Error = %s.", e.getMessage())
            );
        }
    }

    @Override
    public String completeRegistrationOrLogin(MultiValueMap<String, String> params) {
        try {
            var redirectUrl = new AtomicReference<>(uiUriService.getDashboardUriWithNotSuccess());
            SteemConnectCallback meCallback = HiveSignerUtil.getMeCallback(hiveSignerAccountData -> {
                JwtTokenPair jwtTokenPair = registrationOrLogin(hiveSignerAccountData);
                redirectUrl.set(uiUriService.getDashboardUriWithSuccess(jwtTokenPair));
            }, mapper);

            SteemConnectCallback refreshTokenCallback = HiveSignerUtil.getRefreshTokenCallback(hiveSignerTokenPair -> {
                SteemConnectOptions steemConnectOptions = hiveSignerProperties.getSteemConnectOptions(hiveSignerTokenPair.getAccessToken());
                SteemConnect steemConnect = new SteemConnect(steemConnectOptions);
                steemConnect.me(meCallback);
            }, mapper);

            SteemConnect steemConnect = new SteemConnect(hiveSignerProperties.getSteemConnectOptions());
            steemConnect.requestRefreshToken(params.getFirst("code"), refreshTokenCallback);

            return redirectUrl.toString();
        } catch (Exception e) {
            log.error("Error while on complete registration or login by hive signer. Error = {}", e.getMessage());
            return new AtomicReference<>(uiUriService.getDashboardUriWithError()).toString();
        }
    }

    public JwtTokenPair registrationOrLogin(HiveSignerAccountData hiveSignerAccountData) {
        AccountDto accountDto = accountCommunicationService.createOrGet(hiveSignerAccountData.getName());

        var accountAuthenticationOptional = accountAuthenticationRepository.findByAccountId(accountDto.getId());
        if (accountAuthenticationOptional.isEmpty()) {
            JwtTokenPair jwtTokenPair = jwtService.generatePair(accountDto.getId(), defaultAuthenticationRoles);

            AccountAuthentication accountAuthentication = AccountAuthentication.of(
                    accountDto.getId(), jwtTokenPair, defaultAuthenticationRoles, utcClock
            );
            accountAuthenticationRepository.save(accountAuthentication);

            accountKafkaSender.init(accountDto);

            return jwtTokenPair;
        }

        AccountAuthentication accountAuthentication = accountAuthenticationOptional.get();
        JwtTokenPair jwtTokenPair = jwtService.generatePair(accountAuthentication);

        accountAuthentication.updateTokenPair(jwtTokenPair);
        accountAuthentication.setLastModifiedDate(utcClock.millis());
        accountAuthenticationRepository.save(accountAuthentication);

        return jwtTokenPair;
    }

}
