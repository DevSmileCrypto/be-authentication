package io.cryptobrewmaster.ms.be.authentication.integration.account.uri;

import java.net.URI;

/**
 * The interface Account uri service.
 */
public interface AccountUriService {

    /**
     * Gets create uri.
     *
     * @param wallet the wallet
     * @return the create uri
     */
    URI getCreateOrGetUri(String wallet);

}
