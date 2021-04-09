package io.cryptobrewmaster.ms.be.authentication.util.hive.signer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hapramp.steemconnect4j.SteemConnectCallback;
import com.hapramp.steemconnect4j.SteemConnectException;
import io.cryptobrewmaster.ms.be.authentication.model.hive.signer.HiveSignerAccountData;
import io.cryptobrewmaster.ms.be.authentication.model.hive.signer.HiveSignerTokenPair;
import io.cryptobrewmaster.ms.be.library.exception.InnerServiceException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class HiveSignerUtil {

    public static SteemConnectCallback getRefreshTokenCallback(Consumer<HiveSignerTokenPair> onSuccess, ObjectMapper mapper) {
        return new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
                try {
                    var hiveSignerTokenPair = mapper.readValue(s, HiveSignerTokenPair.class);
                    onSuccess.accept(hiveSignerTokenPair);
                } catch (Exception e) {
                    throw new InnerServiceException(
                            String.format("Error while on get refresh token from hive signer. Error = %s", e.getMessage())
                    );
                }
            }

            @Override
            public void onError(SteemConnectException e) {
                throw new InnerServiceException(
                        String.format("Error while get me info from hive signer. Name = %s, description = %s, " +
                                "error = %s. Error = %s.", e.name, e.description, e.error, e.getMessage())
                );
            }
        };
    }

    public static SteemConnectCallback getMeCallback(Consumer<HiveSignerAccountData> onSuccess, ObjectMapper mapper) {
        return new SteemConnectCallback() {
            @Override
            public void onResponse(String s) {
                try {
                    var hiveSignerUserData = mapper.readValue(s, HiveSignerAccountData.class);
                    onSuccess.accept(hiveSignerUserData);
                } catch (Exception e) {
                    throw new InnerServiceException(
                            String.format("Error while on get me info from hive signer. Error = %s", e.getMessage())
                    );
                }
            }

            @Override
            public void onError(SteemConnectException e) {
                throw new InnerServiceException(
                        String.format("Error while get me info from hive signer. Name = %s, description = %s, " +
                                "error = %s. Error = %s.", e.name, e.description, e.error, e.getMessage())
                );
            }
        };
    }

}
