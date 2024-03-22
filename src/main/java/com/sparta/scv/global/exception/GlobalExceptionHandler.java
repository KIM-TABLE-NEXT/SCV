package com.sparta.scv.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionReportDto> handleIllegalArgumentException(
        IllegalArgumentException ex) {
        ExceptionReportDto reportDto = new ExceptionReportDto(ex.getMessage(),
            HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(reportDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BoardAccessDeniedException.class)
    public ResponseEntity<ExceptionReportDto> handleBoardAccessDeniedException(
        BoardAccessDeniedException ex) {
        ExceptionReportDto reportDto = new ExceptionReportDto(ex.getMessage(),
            HttpStatus.FORBIDDEN.value());
        return new ResponseEntity<>(reportDto, HttpStatus.FORBIDDEN);
    }


}
