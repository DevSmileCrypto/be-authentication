package io.cryptobrewmaster.ms.be.authentication.exception;

public class ParametersAbsentOrInvalidException extends BaseException {

    public ParametersAbsentOrInvalidException(String message, Throwable throwable) {
        super(ExceptionCode.SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION, message, throwable);
    }

    public ParametersAbsentOrInvalidException(String message) {
        super(ExceptionCode.SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION, message);
    }


}
