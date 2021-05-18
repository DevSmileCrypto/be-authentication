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

}
