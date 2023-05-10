package com.reggie.takeout.exception;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public R<Object> customExceptionHandler(CustomException e) {
        e.printStackTrace();
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Object> exceptionHandler(Exception e) {
        e.printStackTrace();
        return R.error(e.getMessage());
    }
}
