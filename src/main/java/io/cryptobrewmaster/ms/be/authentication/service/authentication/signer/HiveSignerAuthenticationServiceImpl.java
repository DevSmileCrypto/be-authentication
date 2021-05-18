package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import io.cryptobrewmaster.ms.be.authentication.service.authentication.signer.strategy.HiveSignerAuthenticationStrategy;
import io.cryptobrewmaster.ms.be.authentication.util.OAuthUtil;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class HiveSignerAuthenticationServiceImpl implements HiveSignerAuthenticationService {

    private final List<HiveSignerAuthenticationStrategy> hiveSignerAuthenticationStrategies;

    private HiveSignerAuthenticationStrategy getStrategy(GatewayType type) {
        return hiveSignerAuthenticationStrategies.stream()
                .filter(strategy -> strategy.getType() == type)
                .findFirst()
                .orElseThrow(() -> new InnerServiceException(
                        String.format("Hive signer authentication strategy with type = %s not exists in system", type)
                ));
    }

    @Override
    public String generateLoginUrl(GatewayType type) {
        return getStrategy(type)
                .generateLoginUrl();
    }

    @Override
    public String completeRegistrationOrLogin(MultiValueMap<String, String> params, GatewayType type) {
        return getStrategy(type)
                .completeRegistrationOrLogin(params);
    }

}
