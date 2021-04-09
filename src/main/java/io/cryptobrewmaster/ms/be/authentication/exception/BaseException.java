package io.cryptobrewmaster.ms.be.authentication.exception;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    protected BaseException(ExceptionCode exceptionCode, String message, Throwable throwable) {
        super(message, throwable);
        this.exceptionCode = exceptionCode;
    }

    protected BaseException(ExceptionCode exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
