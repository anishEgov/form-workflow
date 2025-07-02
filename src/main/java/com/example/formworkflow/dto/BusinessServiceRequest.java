package com.example.formworkflow.dto;

import com.example.formworkflow.model.BusinessService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BusinessServiceRequest {

    @NotNull(message = "RequestInfo cannot be null")
    private RequestInfo requestInfo;

    @NotEmpty(message = "At least one BusinessService must be provided")
    private List<@Valid BusinessService> businessServices;
}
