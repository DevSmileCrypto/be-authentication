package io.cryptobrewmaster.ms.be.authentication.properties.hive;

import com.hapramp.steemconnect4j.SteemConnectOptions;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "hive.signer")
public class HiveSignerProperties {

    private String baseUrl;
    private String callbackUrl;
    private String app;
    private String[] scope;
    private String clientSecret;

    public SteemConnectOptions getSteemConnectOptions() {
        SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
        steemConnectOptions.setBaseUrl(getBaseUrl());
        steemConnectOptions.setCallback(getCallbackUrl());
        steemConnectOptions.setApp(getApp());
        steemConnectOptions.setScope(getScope());
        steemConnectOptions.setClientSecret(getClientSecret());
        return steemConnectOptions;
    }

    public SteemConnectOptions getSteemConnectOptions(String accessToken) {
        SteemConnectOptions steemConnectOptions = new SteemConnectOptions();
        steemConnectOptions.setBaseUrl(getBaseUrl());
        steemConnectOptions.setCallback(getCallbackUrl());
        steemConnectOptions.setApp(getApp());
        steemConnectOptions.setScope(getScope());
        steemConnectOptions.setClientSecret(getClientSecret());
        steemConnectOptions.setAccessToken(accessToken);
        return steemConnectOptions;
    }

}
