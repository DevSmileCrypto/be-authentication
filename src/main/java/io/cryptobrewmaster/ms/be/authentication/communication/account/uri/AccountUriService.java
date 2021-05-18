package io.cryptobrewmaster.ms.be.authentication.communication.account.uri;

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

    /**
     * Gets initialize uri.
     *
     * @param accountId the account id
     * @return the initialize uri
     */
    URI getInitializeUri(String accountId);

}
