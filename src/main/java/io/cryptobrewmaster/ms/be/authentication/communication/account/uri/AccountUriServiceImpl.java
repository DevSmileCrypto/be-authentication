package io.cryptobrewmaster.ms.be.authentication.communication.account.uri;

import io.cryptobrewmaster.ms.be.authentication.communication.account.properties.AccountProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@Service
public class AccountUriServiceImpl implements AccountUriService {

    private final AccountProperties accountProperties;

    @Override
    public URI getCreateOrGetUri(String wallet) {
        return UriComponentsBuilder.fromUriString(accountProperties.getUri())
                .path(accountProperties.getPath().getAccount().getCreateOrGet())
                .buildAndExpand(wallet)
                .encode()
                .toUri();
    }

    @Override
    public URI getInitializeUri(String accountId) {
        return UriComponentsBuilder.fromUriString(accountProperties.getUri())
                .path(accountProperties.getPath().getAccount().getInitialize())
                .buildAndExpand(accountId)
                .encode()
                .toUri();
    }

}
