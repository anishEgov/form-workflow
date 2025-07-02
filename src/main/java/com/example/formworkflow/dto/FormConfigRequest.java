package com.example.formworkflow.dto;

import java.util.Map;

public record FormConfigRequest(
        String formType,
        Map<String,Object> schema,
        String tenantId
) {}


