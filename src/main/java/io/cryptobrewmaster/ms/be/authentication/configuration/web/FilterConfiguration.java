package io.cryptobrewmaster.ms.be.authentication.configuration.web;

import io.cryptobrewmaster.ms.be.library.configuration.web.filter.MDCFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfiguration {

    @Bean
    public MDCFilter mdcFilter() {
        return new MDCFilter();
    }

}
