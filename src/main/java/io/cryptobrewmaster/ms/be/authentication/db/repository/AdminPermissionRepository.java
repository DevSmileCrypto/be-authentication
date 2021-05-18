package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AdminPermission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AdminPermissionRepository extends MongoRepository<AdminPermission, String> {

    Optional<AdminPermission> findByWalletAndAllowedTrue(String wallet);

}
