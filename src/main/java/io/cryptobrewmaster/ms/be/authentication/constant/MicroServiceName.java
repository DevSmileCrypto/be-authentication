package io.cryptobrewmaster.ms.be.authentication.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MicroServiceName {

    BE_ACCOUNT("be-account");

    private final String providerName;

}
