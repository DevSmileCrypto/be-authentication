package io.cryptobrewmaster.ms.be.authentication.integration.account.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ms.account")
public class AccountProperties {

    private String uri;
    private Path path;
    private Timeout timeout;

    @Getter
    @Setter
    public static class Path {
        private Account account;

        @Getter
        @Setter
        public static class Account {
            private String createOrGet;
        }
    }

    @Getter
    @Setter
    public static class Timeout {
        private Integer connect;
        private Integer read;
    }

}
