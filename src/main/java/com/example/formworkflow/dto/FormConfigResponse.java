package com.example.formworkflow.dto;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record FormConfigResponse(
        UUID id,
        String formType,
        Map<String,Object> schema,
        String tenantId,
        String createdBy,
        Instant createdTime
) {}
