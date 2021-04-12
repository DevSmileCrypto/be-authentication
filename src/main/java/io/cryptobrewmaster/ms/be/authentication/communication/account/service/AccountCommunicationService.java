package io.cryptobrewmaster.ms.be.authentication.communication.account.service;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;

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
