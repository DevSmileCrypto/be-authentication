package io.cryptobrewmaster.ms.be.authentication.exception;

import io.cryptobrewmaster.ms.be.library.exception.BaseException;
import io.cryptobrewmaster.ms.be.library.exception.dto.ErrorInfoDto;
import io.cryptobrewmaster.ms.be.library.exception.integration.RemoteMsPassThroughException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import static io.cryptobrewmaster.ms.be.library.exception.constants.ExceptionCode.SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION;
import static io.cryptobrewmaster.ms.be.library.exception.constants.ExceptionCode.UNKNOWN_EXCEPTION;
import static io.cryptobrewmaster.ms.be.library.exception.util.ControllerExceptionLogger.log;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorInfoDto> handleBaseException(BaseException e, HttpServletRequest request) {
        log(request, e);
        return new ResponseEntity<>(
                ErrorInfoDto.of(e),
                e.getExceptionCode().getHttpStatus()
        );
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ErrorInfoDto> handleServletException(ServletException e, HttpServletRequest request) {
        log(request, e);
        return new ResponseEntity<>(
                ErrorInfoDto.of(SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfoDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        log(request, e);
        return new ResponseEntity<>(
                ErrorInfoDto.of(SOME_PARAMETERS_ABSENT_OR_INVALID_EXCEPTION),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorInfoDto> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log(request, e);
        return new ResponseEntity<>(
                ErrorInfoDto.of(UNKNOWN_EXCEPTION),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(RemoteMsPassThroughException.class)
    public ResponseEntity<ErrorInfoDto> handleRemoteMsPassThroughException(RemoteMsPassThroughException e, HttpServletRequest request) {
        log(request, e);
        return new ResponseEntity<>(
                e.getErrorInfoDto(),
                HttpStatus.valueOf(e.getErrorInfoDto().getStatus())
        );
    }

}
