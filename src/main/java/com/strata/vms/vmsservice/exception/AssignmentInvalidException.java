package com.strata.vms.vmsservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AssignmentInvalidException extends RuntimeException {

    public AssignmentInvalidException(String message) {
        super(message);
    }
}
