package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AdminPermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AdminPermissionRepository extends ReactiveMongoRepository<AdminPermission, String> {

    Mono<AdminPermission> findByWalletAndAllowedTrue(String wallet);

}
