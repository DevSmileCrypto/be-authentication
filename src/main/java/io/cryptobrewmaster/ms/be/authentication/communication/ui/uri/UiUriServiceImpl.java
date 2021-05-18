package io.cryptobrewmaster.ms.be.authentication.communication.ui.uri;

import io.cryptobrewmaster.ms.be.authentication.communication.ui.properties.UiProperties;
import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Service
public class UiUriServiceImpl implements UiUriService {

    private static final String SUCCESS_PARAM = "success";
    private static final String ERROR_PARAM = "error";

    private final UiProperties uiProperties;

    @Override
    public URI getDashboardUriWithNotSuccess() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getApiDashboard())
                .queryParam(SUCCESS_PARAM, false)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getDashboardUriWithSuccess(JwtTokenPair jwtTokenPair) {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getApiDashboard())
                .queryParams(getJwtQueryParamsMap(jwtTokenPair))
                .queryParam(SUCCESS_PARAM, true)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getDashboardUriWithError() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getApiDashboard())
                .queryParam(ERROR_PARAM, true)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getAdminDashboardUriWithNotSuccess() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getAdminDashboard())
                .queryParam(SUCCESS_PARAM, false)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getAdminDashboardUriWithSuccess(JwtTokenPair jwtTokenPair) {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getAdminDashboard())
                .queryParams(getJwtQueryParamsMap(jwtTokenPair))
                .queryParam(SUCCESS_PARAM, true)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getAdminDashboardUriWithError() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getAdminDashboard())
                .queryParam(ERROR_PARAM, true)
                .build()
                .encode()
                .toUri();
    }

    private MultiValueMap<String, String> getJwtQueryParamsMap(JwtTokenPair jwtTokenPair) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        Optional.ofNullable(jwtTokenPair.getAccessToken()).ifPresent(addQueryParam("accessToken", queryParams));
        Optional.ofNullable(jwtTokenPair.getExpirationAccessTokenDate()).ifPresent(addQueryParam("expirationAccessTokenDate", queryParams));
        Optional.ofNullable(jwtTokenPair.getRefreshToken()).ifPresent(addQueryParam("refreshToken", queryParams));
        Optional.ofNullable(jwtTokenPair.getExpirationRefreshTokenDate()).ifPresent(addQueryParam("expirationRefreshTokenDate", queryParams));
        return queryParams;
    }

    private <T> Consumer<Object> addQueryParam(T queryParamName, MultiValueMap<T, String> queryParams) {
        return o -> queryParams.add(queryParamName, String.valueOf(o));
    }

}
