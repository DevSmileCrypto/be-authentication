package io.cryptobrewmaster.ms.be.authentication.properties.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private Crypto crypto;
    private Token token;

    @Getter
    @Setter
    public static class Crypto {
        private String secretKey;
    }

    @Getter
    @Setter
    public static class Token {
        private String secretKey;
        private Access access;
        private Refresh refresh;

        @Getter
        @Setter
        public static class Access {
            private Integer expireLength;

            public Long getLongExpireLength() {
                return expireLength.longValue();
            }
        }

        @Getter
        @Setter
        public static class Refresh {
            private Integer expireLength;

            public Long getLongExpireLength() {
                return expireLength.longValue();
            }
        }
    }

}
