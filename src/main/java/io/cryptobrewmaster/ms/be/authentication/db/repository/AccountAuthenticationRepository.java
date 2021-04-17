package io.cryptobrewmaster.ms.be.authentication.db.repository;

import io.cryptobrewmaster.ms.be.authentication.db.model.AccountAuthentication;
import io.cryptobrewmaster.ms.be.library.exception.ParametersAbsentOrInvalidException;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AccountAuthenticationRepository extends MongoRepository<AccountAuthentication, String> {

    Optional<AccountAuthentication> findByUid(String uid);

    default AccountAuthentication getByUid(String uid) {
        return findByUid(uid)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with uid = %s not exists in system",
                                uid)
                ));
    }

    Optional<AccountAuthentication> findByUidAndRefreshToken(String uid, String refreshToken);

    default AccountAuthentication getByUidAndRefreshToken(String uid, String refreshToken) {
        return findByUidAndRefreshToken(uid, refreshToken)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with uid = %s and refresh token = %s not exists in system",
                                uid, refreshToken)
                ));
    }

    Optional<AccountAuthentication> findByUidAndAccessToken(String uid, String accessToken);

    default AccountAuthentication getByUidAndAccessToken(String uid, String accessToken) {
        return findByUidAndAccessToken(uid, accessToken)
                .orElseThrow(() -> new ParametersAbsentOrInvalidException(
                        String.format("Account Authentication with uid = %s and access token = %s not exists in system",
                                uid, accessToken)
                ));
    }

}
