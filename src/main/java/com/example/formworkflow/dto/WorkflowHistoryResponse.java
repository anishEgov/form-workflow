package com.example.formworkflow.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkflowHistoryResponse {
    private String fromState;
    private String toState;
    private String action;
    private String comment;
    private String performedBy;
    private String performedAt;  // formatted timestamp (e.g., "2024-06-30T11:24:00Z")
}
