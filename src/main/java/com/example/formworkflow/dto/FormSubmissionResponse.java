package com.example.formworkflow.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FormSubmissionResponse(
        UUID formId,
        UUID workflowInstanceId,
        String status,
        String currentState,
        String submittedBy,
        Long submittedAt
) {}
