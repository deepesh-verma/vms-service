package com.strata.vms.vmsservice.configuration;

import com.strata.vms.vmsservice.exception.AssignmentInvalidException;
import com.strata.vms.vmsservice.model.ApiResponseModel;
import com.strata.vms.vmsservice.model.ErrorResponseModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private ApiResponseModel<?> getApiErrorResponseModel(String message, List<String> errors) {
        String vmsTraceId = "test";
        return ApiResponseModel.of(null, new ErrorResponseModel(vmsTraceId, message, errors));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NotNull MethodArgumentNotValidException ex, @NotNull HttpHeaders headers,
            @NotNull HttpStatusCode status, @NotNull WebRequest request) {

        logger.error("Creating error response for the exception in handleMethodArgumentNotValid", ex);

        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiResponseModel<?> errorResponse = getApiErrorResponseModel(ex.getMessage(), errors);
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AssignmentInvalidException.class)
    public final ResponseEntity<Object> handleAssignmentInvalidException(Exception ex, WebRequest request) {

        logger.error("Creating error response for the exception in handleAssignmentInvalidException", ex);

        ApiResponseModel<?> errorResponse =
                getApiErrorResponseModel(ex.getMessage(), Collections.emptyList());
        return handleExceptionInternal(
                ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {

        logger.error("Creating error response for the exception in handleAll", ex);

        ApiResponseModel<?> errorResponse =
                getApiErrorResponseModel(ex.getMessage(), Collections.emptyList());
        return handleExceptionInternal(
                ex, errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
