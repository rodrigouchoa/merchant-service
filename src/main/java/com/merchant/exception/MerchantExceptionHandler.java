package com.merchant.exception;

import com.merchant.domain.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class MerchantExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<Object> handleNumberFormatException(NumberFormatException ex) {
        log.warn(ex.getMessage(), ex);
        return handleError(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<Object> handleMerchantNotFound(TransactionNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        return handleError(ex, HttpStatus.NOT_FOUND);
    }

    private ResponseEntity<Object> handleError(Exception ex, HttpStatus httpStatus) {
        ErrorDTO errorDTO = ErrorDTO.builder()
                .httpErrorCode(httpStatus.toString())
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDTO, httpStatus);
    }
}
