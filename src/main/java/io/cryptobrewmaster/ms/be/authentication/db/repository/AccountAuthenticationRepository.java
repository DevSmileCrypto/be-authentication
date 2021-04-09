package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountAuthenticationRepository extends MongoRepository<AccountAuthentication, String> {

    Optional<AccountAuthentication> findByAccountId(String accountId);

    default AccountAuthentication getByAccountId(String accountId) {
        return findByAccountId(accountId)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with account id = %s not exists in system",
                                accountId)
                ));
    }

    Optional<AccountAuthentication> findByAccountIdAndRefreshToken(String accountId, String refreshToken);

    default AccountAuthentication getByAccountIdAndRefreshToken(String accountId, String refreshToken) {
        return findByAccountIdAndRefreshToken(accountId, refreshToken)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with account id = %s and refresh token = %s not exists in system",
                                accountId, refreshToken)
                ));
    }

    Optional<AccountAuthentication> findByAccountIdAndAccessToken(String accountId, String accessToken);

    default AccountAuthentication getByAccountIdAndAccessToken(String accountId, String accessToken) {
        return findByAccountIdAndAccessToken(accountId, accessToken)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with account id = %s and access token = %s not exists in system",
                                accountId, accessToken)
                ));
    }

}
