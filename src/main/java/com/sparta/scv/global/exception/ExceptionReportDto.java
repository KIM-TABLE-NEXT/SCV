package com.sparta.scv.global.exception;

import lombok.Getter;

@Getter
public class ExceptionReportDto {
    private String message;
    private int statusCode;

    public ExceptionReportDto(String message, int statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
