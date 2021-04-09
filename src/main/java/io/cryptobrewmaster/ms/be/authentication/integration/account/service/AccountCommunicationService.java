package io.cryptobrewmaster.ms.be.authentication.integration.account.service;

import io.cryptobrewmaster.ms.be.authentication.integration.account.dto.AccountDto;

/**
 * The interface Account communication service.
 */
public interface AccountCommunicationService {

    /**
     * Create account dto.
     *
     * @param wallet the wallet
     * @return the account dto
     */
    AccountDto createOrGet(String wallet);

}
