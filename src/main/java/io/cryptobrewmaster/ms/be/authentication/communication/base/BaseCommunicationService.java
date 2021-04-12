package io.cryptobrewmaster.ms.be.authentication.communication.base;

import io.cryptobrewmaster.ms.be.authentication.communication.base.model.RequestLog;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseCommunicationService {

    private final RestTemplate restTemplate;

    protected <R, T> T performRequestWithResponse(URI uri, HttpMethod method, R request,
                                                  Class<T> responseType, RequestLog requestLog) {
        var httpEntity = HttpEntity.EMPTY;
        if (Objects.nonNull(request)) {
            httpEntity = new HttpEntity<>(request);
        }

        try {
            log.info(requestLog.getPreLogMessage());
            T responseBody = restTemplate.exchange(uri, method, httpEntity, responseType).getBody();
            log.info(requestLog.getPostLogMessage(responseBody));
            return responseBody;
        } catch (ResourceAccessException e) {
            throw new InnerServiceException(requestLog.getErrorLogMessage(e));
        }
    }

    protected <R, T> T performRequestWithResponse(URI uri, HttpMethod method, R request,
                                                  ParameterizedTypeReference<T> responseType,
                                                  RequestLog requestLog) {
        var httpEntity = HttpEntity.EMPTY;
        if (Objects.nonNull(request)) {
            httpEntity = new HttpEntity<>(request);
        }

        try {
            log.info(requestLog.getPreLogMessage());
            T responseBody = restTemplate.exchange(uri, method, httpEntity, responseType).getBody();
            log.info(requestLog.getPostLogMessage(responseBody));
            return responseBody;
        } catch (ResourceAccessException e) {
            throw new InnerServiceException(requestLog.getErrorLogMessage(e));
        }
    }

    protected <R> void performRequestWithoutResponse(URI uri, HttpMethod method, R request, RequestLog requestLog) {
        var httpEntity = HttpEntity.EMPTY;
        if (Objects.nonNull(request)) {
            httpEntity = new HttpEntity<>(request);
        }

        try {
            log.info(requestLog.getPreLogMessage());
            HttpStatus statusCode = restTemplate.exchange(uri, method, httpEntity, String.class).getStatusCode();
            log.info(requestLog.getPostLogMessage(statusCode));
        } catch (ResourceAccessException e) {
            throw new InnerServiceException(requestLog.getErrorLogMessage(e));
        }
    }

    protected <T> T performRequestWithResponse(URI uri, HttpMethod method, Class<T> responseType, RequestLog requestLog) {
        return performRequestWithResponse(uri, method, null, responseType, requestLog);
    }

    protected <T> T performRequestWithResponse(URI uri, HttpMethod method, ParameterizedTypeReference<T> responseType, RequestLog requestLog) {
        return performRequestWithResponse(uri, method, null, responseType, requestLog);
    }

    protected void performRequestWithoutResponse(URI uri, HttpMethod method, RequestLog requestLog) {
        performRequestWithoutResponse(uri, method, null, requestLog);
    }

}
