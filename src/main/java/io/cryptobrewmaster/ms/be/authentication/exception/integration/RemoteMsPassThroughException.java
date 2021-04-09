package io.cryptobrewmaster.ms.be.authentication.exception.integration;

import io.cryptobrewmaster.ms.be.authentication.exception.model.ErrorInfoDto;

public class RemoteMsPassThroughException extends RuntimeException {

    private final ErrorInfoDto errorInfoDto;

    RemoteMsPassThroughException(ErrorInfoDto errorInfoDto) {
        super(errorInfoDto.getMessages().isEmpty() ? "Unknown remote MS error." : errorInfoDto.getMessages().get(0));
        this.errorInfoDto = errorInfoDto;
    }

    public ErrorInfoDto getErrorInfoDto() {
        return errorInfoDto;
    }
}
