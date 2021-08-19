package io.cryptobrewmaster.ms.be.authentication.communication.account.service;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import reactor.core.publisher.Mono;

/**
 * The interface Account communication service.
 */
public interface AccountCommunicationService {

    /**
     * Create or get mono.
     *
     * @param wallet the wallet
     * @return the mono
     */
    Mono<AccountDto> createOrGet(String wallet);

    /**
     * Initialize mono.
     *
     * @param accountId the account id
     * @return the mono
     */
    Mono<AccountDto> initialize(String accountId);

}
