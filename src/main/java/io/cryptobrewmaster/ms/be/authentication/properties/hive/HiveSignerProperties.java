package io.cryptobrewmaster.ms.be.authentication.properties.hive;

import com.hapramp.steemconnect4j.SteemConnectOptions;
import io.cryptobrewmaster.ms.be.library.constants.GatewayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "hive.signer")
public class HiveSignerProperties {

    private String baseUrl;
    private ApiGateway apiGateway;
    private AdminGateway adminGateway;
    private String app;
    private String[] scope;
    private String clientSecret;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ApiGateway {
        private String callbackUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class AdminGateway {
        private String callbackUrl;
    }

    private final Map<GatewayType, Consumer<SteemConnectOptions>> typeCallbackUrlMap = Map.of(
            GatewayType.API, options -> options.setCallback(getApiGateway().getCallbackUrl()),
            GatewayType.ADMIN, options -> options.setCallback(getAdminGateway().getCallbackUrl())
    );

    public SteemConnectOptions getOptions(GatewayType type) {
        return getDefaultOptions(type);
    }

    public SteemConnectOptions getOptionsWithState(String state, GatewayType type) {
        var steemConnectOptions = getDefaultOptions(type);
        steemConnectOptions.setState(state);
        return steemConnectOptions;
    }

    public SteemConnectOptions getOptionsWithAccessToken(String accessToken, GatewayType type) {
        var steemConnectOptions = getDefaultOptions(type);
        steemConnectOptions.setAccessToken(accessToken);
        return steemConnectOptions;
    }

    private SteemConnectOptions getDefaultOptions(GatewayType type) {
        var steemConnectOptions = new SteemConnectOptions();
        steemConnectOptions.setBaseUrl(getBaseUrl());
        steemConnectOptions.setApp(getApp());
        steemConnectOptions.setScope(getScope());
        steemConnectOptions.setClientSecret(getClientSecret());

        typeCallbackUrlMap.get(type).accept(steemConnectOptions);
        return steemConnectOptions;
    }

}
