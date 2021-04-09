package io.cryptobrewmaster.ms.be.authentication.integration.ui.uri;

import io.cryptobrewmaster.ms.be.authentication.model.jwt.JwtTokenPair;

import java.net.URI;

/**
 * The interface Ui uri service.
 */
public interface UiUriService {

    /**
     * Gets dashboard uri with not success.
     *
     * @return the dashboard uri with not success
     */
    URI getDashboardUriWithNotSuccess();

    /**
     * Gets dashboard uri with success.
     *
     * @param jwtTokenPair the jwt token pair
     * @return the dashboard uri with success
     */
    URI getDashboardUriWithSuccess(JwtTokenPair jwtTokenPair);

    /**
     * Gets dashboard uri with error.
     *
     * @return the dashboard uri with error
     */
    URI getDashboardUriWithError();

}
