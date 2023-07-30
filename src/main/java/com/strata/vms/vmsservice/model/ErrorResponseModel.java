package com.strata.vms.vmsservice.model;

import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class ErrorResponseModel {

    @NonNull private String vmsTraceId;

    @NonNull private String message;

    @NonNull List<String> errors;
}