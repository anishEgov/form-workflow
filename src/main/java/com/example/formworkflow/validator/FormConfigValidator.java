package com.example.formworkflow.validator;

import com.example.formworkflow.dto.FormConfigRequest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class FormConfigValidator {

    public void validate(FormConfigRequest request) {
        if (request.formType() == null || request.formType().isBlank()) {
            throw new IllegalArgumentException("formType is required");
        }

        if (request.schema() == null || request.schema().isEmpty()) {
            throw new IllegalArgumentException("schema is required");
        }

        if (request.tenantId() == null || request.tenantId().isBlank()) {
            throw new IllegalArgumentException("tenantId is required");
        }
    }
}
