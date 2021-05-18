package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapramp.steemconnect4j.SteemConnect;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectOptions;
import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.service.AccountCommunicationService;
import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.db.repository.AccountAuthenticationRepository;
import io.cryptobrewmaster.ms.be.authentication.model.hive.signer.HiveSignerAccountData;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import io.cryptobrewmaster.ms.be.authentication.properties.hive.HiveSignerProperties;
import io.cryptobrewmaster.ms.be.authentication.util.OAuthUtil;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import static io.cryptobrewmaster.ms.be.authentication.util.hive.signer.HiveSignerUtil.getMeCallback;
import static io.cryptobrewmaster.ms.be.authentication.util.hive.signer.HiveSignerUtil.getRefreshTokenCallback;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHiveSignerAuthenticationStrategy implements HiveSignerAuthenticationStrategy {

    protected final AccountCommunicationService accountCommunicationService;

    protected final AccountAuthenticationRepository accountAuthenticationRepository;

    protected final HiveSignerProperties hiveSignerProperties;

    protected final ObjectMapper mapper;

    abstract Supplier<URI> getUriWithNotSuccessSupplier();

    abstract Supplier<URI> getUriWithSuccessSupplier(JwtTokenPair jwtTokenPair);

    abstract Supplier<URI> getUriWithErrorSupplier();

    abstract JwtTokenPair login(AccountDto accountDto, Map<String, String> state, AccountAuthentication accountAuthentication);

    abstract JwtTokenPair registration(AccountDto accountDto, Map<String, String> state);

    @Override
    public String generateLoginUrl() {
        try {
            SteemConnectOptions steemConnectOptions = hiveSignerProperties.getOptionsWithState(
                    OAuthUtil.generateState(getType()), getType()
            );
            return new SteemConnect(steemConnectOptions).getLoginUrl(true);
        } catch (Exception e) {
            throw new InnerServiceException(
                    String.format("Error while on generate hive signer login url. Error = %s.", e.getMessage())
            );
        }
    }

    @Override
    public String completeRegistrationOrLogin(MultiValueMap<String, String> params) {
        try {
            var redirectUrl = new AtomicReference<>(getUriWithNotSuccessSupplier().get());
            SteemConnectCallback meCallback = getMeCallback(hiveSignerAccountData -> {
                var jwtTokenPair = registrationOrLogin(
                        hiveSignerAccountData, OAuthUtil.parseState(params.getFirst("state"))
                );
                redirectUrl.set(getUriWithSuccessSupplier(jwtTokenPair).get());
            }, mapper);

            SteemConnectCallback refreshTokenCallback = getRefreshTokenCallback(hiveSignerTokenPair -> new SteemConnect(
                    hiveSignerProperties.getOptionsWithAccessToken(hiveSignerTokenPair.getAccessToken(), getType())
            ).me(meCallback), mapper);

            var steemConnect = new SteemConnect(hiveSignerProperties.getOptions(getType()));
            steemConnect.requestRefreshToken(params.getFirst("code"), refreshTokenCallback);

            return redirectUrl.toString();
        } catch (Exception e) {
            log.error("Error while on complete registration or login by hive signer. Error = {}", e.getMessage());
            return new AtomicReference<>(getUriWithErrorSupplier().get()).toString();
        }
    }

    private JwtTokenPair registrationOrLogin(HiveSignerAccountData hiveSignerAccountData, Map<String, String> state) {
        var accountDto = accountCommunicationService.createOrGet(hiveSignerAccountData.getName());

        return accountAuthenticationRepository.findByAccountId(accountDto.getId())
                .map(accountAuthentication -> login(accountDto, state, accountAuthentication))
                .orElseGet(() -> registration(accountDto, state));
    }

}
