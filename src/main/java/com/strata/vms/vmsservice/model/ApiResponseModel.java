package com.strata.vms.vmsservice.model;

import java.util.List;
import lombok.Data;

@Data(staticConstructor = "of")
public class ApiResponseModel<T> {

    private final List<T> data;

    private final ErrorResponseModel error;
}
