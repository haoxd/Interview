package com.bread.coalquality.sys.config;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.bread.coalquality.sys.common.RespInfo;
import com.bread.coalquality.sys.constans.Constants;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/***
 * 全局异常处理
 * @author haoxd
 * **/
@Slf4j
public class GlobalExceptionTranslator {


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public RespInfo handleError(MissingServletRequestParameterException e) {
        log.warn("Missing Request Parameter", e);
        String message = String.format("Missing Request Parameter: %s", e.getParameterName());
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public RespInfo handleError(MethodArgumentTypeMismatchException e) {
        log.warn("Method Argument Type Mismatch", e);
        String message = String.format("Method Argument Type Mismatch: %s", e.getName());
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(message)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RespInfo handleError(MethodArgumentNotValidException e) {
        //log.warn("Method Argument Not Valid", e);
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(message)
                .build();
    }

    @ExceptionHandler(BindException.class)
    public RespInfo handleError(BindException e) {
        log.warn("Bind Exception", e);
        FieldError error = e.getFieldError();
        String message = String.format("%s:%s", error.getField(), error.getDefaultMessage());
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(message)
                .build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public RespInfo handleError(ConstraintViolationException e) {
        log.warn("Constraint Violation", e);
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        ConstraintViolation<?> violation = violations.iterator().next();
        String path = ((PathImpl) violation.getPropertyPath()).getLeafNode().getName();
        String message = String.format("%s:%s", path, violation.getMessage());
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(message)
                .build();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public RespInfo handleError(NoHandlerFoundException e) {
        log.error("404 Not Found", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public RespInfo handleError(HttpMessageNotReadableException e) {
        log.error("Message Not Readable", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RespInfo handleError(HttpRequestMethodNotSupportedException e) {
        log.error("Request Method Not Supported", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public RespInfo handleError(HttpMediaTypeNotSupportedException e) {
        log.error("Media Type Not Supported", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }


    @ExceptionHandler(ApiException.class)
    public RespInfo handleError(ApiException e) {
        log.error("ApiException ", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }

    @ExceptionHandler(NullPointerException.class)
    public RespInfo handleError(NullPointerException e) {
        log.error("NullPointer Exception", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();

    }


    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RespInfo handleError(Exception e) {
        log.error("Exception ", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();

    }

    @ExceptionHandler(Throwable.class)
    public RespInfo handleError(Throwable e) {
        log.error("Internal Server Error", e);
        return RespInfo
                .builder()
                .respCode(Constants.FAIL)
                .respDesc(e.getMessage())
                .build();
    }
}
