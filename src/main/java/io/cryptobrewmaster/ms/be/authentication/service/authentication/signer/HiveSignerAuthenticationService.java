package io.cryptobrewmaster.ms.be.authentication.service.authentication.signer;

import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.view.RedirectView;

/**
 * The interface Hive signer authentication service.
 */
public interface HiveSignerAuthenticationService {

    /**
     * Generate login url redirect view.
     *
     * @return the redirect view
     */
    RedirectView generateLoginUrl();

    /**
     * Complete registration or login redirect view.
     *
     * @param params the params
     * @return the redirect view
     */
    RedirectView completeRegistrationOrLogin(MultiValueMap<String, String> params);

}
