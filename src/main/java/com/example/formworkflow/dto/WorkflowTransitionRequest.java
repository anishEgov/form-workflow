package com.example.formworkflow.dto;
import com.example.formworkflow.dto.RequestInfo;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowTransitionRequest {

    @NotNull(message = "RequestInfo cannot be null")
    private RequestInfo requestInfo;

    @NotEmpty(message = "ProcessInstances list cannot be empty")
    private List<@Valid ProcessInstance> processInstances;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProcessInstance {

        @NotBlank
        private String moduleName;

        @NotBlank
        private String tenantId;

        @NotBlank
        private String businessService;

        @NotBlank
        private String formInstanceId;

        @NotBlank
        private String action;

        private String comment;
        private List<String> assignees;
        private String previousStatus;
    }
}
