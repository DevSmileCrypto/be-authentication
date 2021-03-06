package io.cryptobrewmaster.ms.be.authentication.communication.ui.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "ms.ui")
public class UiProperties {

    private String uri;
    private Path path;
    private Timeout timeout;

    @Getter
    @Setter
    public static class Path {
        private String apiDashboard;
        private String adminDashboard;
    }

    @Getter
    @Setter
    public static class Timeout {
        private Integer connect;
        private Integer read;
    }

}
