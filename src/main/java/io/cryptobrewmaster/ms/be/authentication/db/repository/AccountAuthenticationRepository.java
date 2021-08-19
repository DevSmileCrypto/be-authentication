package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AccountAuthenticationRepository extends ReactiveMongoRepository<AccountAuthentication, String> {

    Mono<AccountAuthentication> findByAccountId(String accountId);

}
