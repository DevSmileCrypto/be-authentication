package io.cryptobrewmaster.ms.be.authentication.properties.hive;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(value = "hive.keychain")
public class HiveKeychainProperties {

    private Validator validator;

    @Getter
    @Setter
    public static class Validator {
        private String nodePath;
        private String filePath;
        private String method;
        private Long timeout;

        public Long getTimeoutInMs() {
            return timeout * 1000;
        }
    }

}
