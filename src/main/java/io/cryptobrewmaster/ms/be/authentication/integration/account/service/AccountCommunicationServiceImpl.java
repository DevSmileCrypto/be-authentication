package io.cryptobrewmaster.ms.be.authentication.integration.account.service;

import io.cryptobrewmaster.ms.be.authentication.integration.account.dto.AccountDto;
import io.cryptobrewmaster.ms.be.authentication.integration.account.uri.AccountUriService;
import io.cryptobrewmaster.ms.be.library.constants.MicroServiceName;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccountCommunicationServiceImpl implements AccountCommunicationService {

    private final RestTemplate accountRestTemplate;
    private final AccountUriService accountUriService;

    private String getMicroServiceName() {
        return MicroServiceName.BE_ACCOUNT.getProviderName();
    }

    @Override
    public AccountDto createOrGet(String wallet) {
        var uri = accountUriService.getCreateOrGetUri(wallet);

        try {
            log.info("Request to create or get account by wallet send to {} ms. Wallet = {}", getMicroServiceName(), wallet);
            AccountDto accountDto = accountRestTemplate.exchange(uri, POST, HttpEntity.EMPTY, AccountDto.class).getBody();
            log.info("Response on create or get account by wallet from {} ms. {}", getMicroServiceName(), accountDto);
            return accountDto;
        } catch (ResourceAccessException e) {
            throw new InnerServiceException(
                    String.format("No response from %s ms on create or get account by wallet request. " +
                            "Wallet = %s. Error = %s", getMicroServiceName(), wallet, e.getMessage())
            );
        }
    }

}
