package io.cryptobrewmaster.ms.be.authentication.integration.ui.uri;

import io.cryptobrewmaster.ms.be.authentication.integration.ui.properties.UiProperties;
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

    private final UiProperties uiProperties;

    @Override
    public URI getDashboardUriWithNotSuccess() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getDashboard())
                .queryParam("success", false)
                .build()
                .encode()
                .toUri();
    }

    @Override
    public URI getDashboardUriWithSuccess(JwtTokenPair jwtTokenPair) {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getDashboard())
                .queryParams(getJwtQueryParamsMap(jwtTokenPair))
                .queryParam("success", true)
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

    @Override
    public URI getDashboardUriWithError() {
        return UriComponentsBuilder.fromUriString(uiProperties.getUri())
                .path(uiProperties.getPath().getDashboard())
                .queryParam("error", true)
                .build()
                .encode()
                .toUri();
    }

}
