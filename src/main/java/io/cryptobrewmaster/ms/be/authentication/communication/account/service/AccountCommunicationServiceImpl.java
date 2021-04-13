package io.cryptobrewmaster.ms.be.authentication.communication.account.service;

import io.cryptobrewmaster.ms.be.authentication.communication.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.communication.account.uri.AccountUriService;
import io.cryptobrewmaster.ms.be.library.communication.BaseCommunicationService;
import io.cryptobrewmaster.ms.be.library.communication.model.RequestLog;
import io.cryptobrewmaster.ms.be.library.constants.MicroServiceName;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AccountCommunicationServiceImpl extends BaseCommunicationService implements AccountCommunicationService {

    private final AccountUriService accountUriService;

    public AccountCommunicationServiceImpl(RestTemplate accountRestTemplate,
                                           AccountUriService accountUriService) {
        super(accountRestTemplate);
        this.accountUriService = accountUriService;
    }

    String getMicroServiceName() {
        return MicroServiceName.BE_ACCOUNT.getProviderName();
    }

    @Override
    public AccountDto createOrGet(String wallet) {
        return performRequestWithResponse(
                accountUriService.getCreateOrGetUri(wallet),
                HttpMethod.POST,
                AccountDto.class,
                new RequestLog(
                        "Request to create or get account by wallet send to %s ms. Wallet = %s",
                        List.of(getMicroServiceName(), wallet),
                        "Response on create or get account by wallet from %s ms.",
                        List.of(getMicroServiceName()),
                        "No response from %s ms on create or get account by wallet request. Wallet = %s.",
                        List.of(getMicroServiceName(), wallet)
                )
        );
    }

}
