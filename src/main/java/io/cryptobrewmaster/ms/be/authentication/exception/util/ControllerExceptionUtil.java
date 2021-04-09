package io.cryptobrewmaster.ms.be.authentication.exception.util;

import io.cryptobrewmaster.ms.be.library.exception.BaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ControllerExceptionUtil {

    private static final String MESSAGE_PREFIX = " | Message: ";

    public static void log(HttpServletRequest request, BaseException baseException) {
        String message = prepareErrorMessage(request, baseException.getMessage());
        if (baseException.getExceptionCode().getHttpStatus() == HttpStatus.INTERNAL_SERVER_ERROR) {
            log.error(message);
        } else {
            log.warn(message);
        }
    }

    public static String prepareErrorMessage(HttpServletRequest request, String errorMessage) {
        return request.getRequestURI() + "?" + request.getQueryString() + MESSAGE_PREFIX + errorMessage;
    }

}
