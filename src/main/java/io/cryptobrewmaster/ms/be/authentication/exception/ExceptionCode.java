package io.cryptobrewmaster.ms.be.authentication.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION(4000101, "Some parameters are absent, or invalid.", HttpStatus.BAD_REQUEST),

    INTERNAL_SERVICE_EXCEPTION(5000101, "Issue with internal service.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNKNOWN_EXCEPTION(999990199, "Unknown error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String debugMessage;
    private final HttpStatus httpStatus;

}
