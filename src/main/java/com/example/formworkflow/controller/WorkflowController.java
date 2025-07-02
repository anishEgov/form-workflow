package com.example.formworkflow.controller;

import com.example.formworkflow.dto.*;
import com.example.formworkflow.model.WorkflowInstance;
import com.example.formworkflow.service.WorkflowInstanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/wf/process")
@RequiredArgsConstructor
@Validated
public class WorkflowController {

    private final WorkflowInstanceService workflowInstanceService;

    @PostMapping("/_transition")
    public ResponseEntity<?> transition(@Valid @RequestBody WorkflowTransitionRequest request) {
        RequestInfo requestInfo = request.getRequestInfo();
        UserInfo user = requestInfo.getUserInfo();

        List<WorkflowInstanceResponse> updatedInstances = request.getProcessInstances().stream()
                .map(instance -> workflowInstanceService.transition(instance, user))
                .toList();

        return ResponseEntity.ok(Map.of(
                "ResponseInfo", Map.of("status", "successful"),
                "ProcessInstances", updatedInstances
        ));
    }

    @GetMapping("/instance/{formId}")
    public ResponseEntity<?> getCurrentWorkflowInstance(
            @PathVariable String formId,
            @RequestParam String tenantId
    ) {
        return ResponseEntity.ok(Map.of(
                "ResponseInfo", Map.of("status", "successful"),
                "ProcessInstance", workflowInstanceService.getByFormIdAndTenantId(formId, tenantId)
        ));
    }

    @GetMapping("/history/{formId}")
    public ResponseEntity<?> getWorkflowHistory(
            @PathVariable String formId,
            @RequestParam String tenantId
    ) {
        List<WorkflowHistoryResponse> history = workflowInstanceService.getHistory(formId, tenantId);

        return ResponseEntity.ok(Map.of(
                "ResponseInfo", Map.of("status", "successful"),
                "History", history
        ));
    }

    @GetMapping("/instances/_search")
    public ResponseEntity<?> searchByState(@RequestParam String tenantId,
                                           @RequestParam String state,
                                           @RequestParam(required = false) String businessService) {

        List<WorkflowInstanceResponse> instances = (businessService != null)
                ? workflowInstanceService.getInstancesByStateAndService(tenantId, businessService, state)
                : workflowInstanceService.getInstancesByState(tenantId, state);

        return ResponseEntity.ok(Map.of(
                "ResponseInfo", Map.of("status", "successful"),
                "ProcessInstances", instances
        ));
    }



}
