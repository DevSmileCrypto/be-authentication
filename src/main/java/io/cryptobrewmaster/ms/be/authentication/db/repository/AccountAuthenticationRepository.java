package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.authentication.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountAuthenticationRepository extends MongoRepository<AccountAuthentication, String> {

    default AccountAuthentication getByAccountId(String accountId) {
        return findById(accountId)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with account id = %s not exists in system",
                                accountId)
                ));
    }

}
