package com.example.formworkflow.dto;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record WorkflowInstanceResponse(
        UUID id,
        String formInstanceId,
        String businessService,
        String currentState,
        String previousStatus,
        String comment,
        List<String> assignees,
        String createdBy,
        Long createdTime,
        String lastModifiedBy,
        Long lastModifiedTime,
        String tenantId
) {}
