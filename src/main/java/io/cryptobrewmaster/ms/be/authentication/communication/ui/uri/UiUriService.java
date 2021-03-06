package io.cryptobrewmaster.ms.be.authentication.communication.ui.uri;

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

    /**
     * Gets admin dashboard uri with not success.
     *
     * @return the admin dashboard uri with not success
     */
    URI getAdminDashboardUriWithNotSuccess();

    /**
     * Gets admin dashboard uri with success.
     *
     * @param jwtTokenPair the jwt token pair
     * @return the admin dashboard uri with success
     */
    URI getAdminDashboardUriWithSuccess(JwtTokenPair jwtTokenPair);

    /**
     * Gets admin dashboard uri with error.
     *
     * @return the admin dashboard uri with error
     */
    URI getAdminDashboardUriWithError();

}
