package com.reggie.takeout.exception;

import com.reggie.takeout.common.R;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProjectExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public R<Object> customExceptionHandler(CustomException e) {
        return R.error(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R<Object> exceptionHandler(Exception e) {
        return R.error(e.getMessage());
    }
}
