package io.cryptobrewmaster.ms.be.authentication.exception;

public class InnerServiceException extends BaseException {

    public InnerServiceException(String message, Throwable throwable) {
        super(ExceptionCode.INTERNAL_SERVICE_EXCEPTION, message, throwable);
    }

    public InnerServiceException(String message) {
        super(ExceptionCode.INTERNAL_SERVICE_EXCEPTION, message);
    }

}
