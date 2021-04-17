package io.cryptobrewmaster.ms.be.authentication.configuration.kafka.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

    private String bootstrapServers;
    private Config config;
    private Topic topic;

    @Getter
    @Setter
    public static class Config {
        private String groupId;
        private String clientId;
        private Boolean enableAutoCommit;
    }

    @Getter
    @Setter
    public static class Topic {
        private String accountInit;
    }
}
