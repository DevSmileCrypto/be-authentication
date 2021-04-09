package io.cryptobrewmaster.ms.be.authentication.exception.integration;

import io.cryptobrewmaster.ms.be.authentication.constant.MicroServiceName;
import io.cryptobrewmaster.ms.be.authentication.exception.model.ErrorInfoDto;

public class AccountErrorHandler extends BasicResponseErrorHandler {

    @Override
    String getServiceName() {
        return MicroServiceName.BE_ACCOUNT.getProviderName();
    }

    @Override
    void handleError(ErrorInfoDto errorInfoDto) {
        throw new RemoteMsPassThroughException(errorInfoDto);
    }

}
