package io.cryptobrewmaster.ms.be.authentication.configuration.rest;

import io.cryptobrewmaster.ms.be.authentication.communication.account.properties.AccountProperties;
import io.cryptobrewmaster.ms.be.authentication.configuration.rest.properties.RestTemplateProperties;
import io.cryptobrewmaster.ms.be.library.configuration.rest.interceptor.JsonContentTypeRestTemplateInterceptor;
import io.cryptobrewmaster.ms.be.library.constants.MicroServiceName;
import io.cryptobrewmaster.ms.be.library.exception.integration.CommunicationErrorHandler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(RestTemplateProperties restTemplateProperties) {
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
        manager.setMaxTotal(restTemplateProperties.getMaxTotal());
        manager.setDefaultMaxPerRoute(restTemplateProperties.getDefaultMaxPerRoute());
        return manager;
    }

    @Bean
    public RequestConfig requestConfig(RestTemplateProperties restTemplateProperties) {
        return RequestConfig.custom()
                .setSocketTimeout(restTemplateProperties.getSocketTimeout())
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager, RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder(HttpClient httpClient) {
        return new RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    @Bean(name = "accountRestTemplate")
    public RestTemplate accountRestTemplate(RestTemplateBuilder restTemplateBuilder, AccountProperties accountProperties) {
        return restTemplateBuilder.errorHandler(new CommunicationErrorHandler(MicroServiceName.BE_ACCOUNT))
                .rootUri(accountProperties.getUri())
                .interceptors(new JsonContentTypeRestTemplateInterceptor())
                .setConnectTimeout(Duration.ofMillis(accountProperties.getTimeout().getConnect()))
                .setReadTimeout(Duration.ofMillis(accountProperties.getTimeout().getRead()))
                .build();
    }

}
