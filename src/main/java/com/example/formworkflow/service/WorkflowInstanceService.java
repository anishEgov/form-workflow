package com.example.formworkflow.service;

import com.example.formworkflow.dto.*;
import com.example.formworkflow.exception.WorkflowException;
import com.example.formworkflow.model.*;
import com.example.formworkflow.repository.BusinessServiceRepository;
import com.example.formworkflow.repository.WorkflowInstanceRepository;
import com.example.formworkflow.repository.WorkflowStateHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkflowInstanceService {

    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final BusinessServiceRepository businessServiceRepository;
    private final WorkflowStateHistoryRepository workflowStateHistoryRepository;


    public WorkflowInstanceResponse transition(WorkflowTransitionRequest.ProcessInstance instance, UserInfo user) {
        log.info("Transition requested for FormInstanceId={} by user={}", instance.getFormInstanceId(), user.getUuid());

        WorkflowInstance existing = workflowInstanceRepository
                .findByTenantIdAndFormInstanceId(instance.getTenantId(), instance.getFormInstanceId())
                .orElseThrow(() -> new WorkflowException("No workflow instance found for the provided form ID."));

        BusinessService config = businessServiceRepository
                .findByBusinessServiceAndTenantId(instance.getBusinessService(), instance.getTenantId())
                .orElseThrow(() -> new WorkflowException("No BusinessService config found."));

        StateDefinition currentState = config.getStates().stream()
                .filter(s -> Objects.equals(s.getState(), existing.getCurrentState()))
                .findFirst()
                .orElseThrow(() -> new WorkflowException("Current state not defined in workflow config."));

        ActionDefinition matchedAction = currentState.getActions().stream()
                .filter(a -> Objects.equals(a.getAction(), instance.getAction()))
                .findFirst()
                .orElseThrow(() -> new WorkflowException("No valid action '" + instance.getAction() + "' defined for current state."));

//        Set<String> userRoles = user.getRoles().stream()
//                .map(Role::getCode)
//                .collect(Collectors.toSet());
        Set<String> userRoles = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());


        boolean authorized = matchedAction.getRoles().stream().anyMatch(userRoles::contains);
        if (!authorized) {
            throw new WorkflowException("User is not authorized to perform the action: " + instance.getAction());
        }

        // Update workflow instance
        existing.setPreviousStatus(existing.getCurrentState());
        existing.setCurrentState(matchedAction.getNextState());
        existing.setAssignees(Optional.ofNullable(instance.getAssignees()).orElse(Collections.emptyList()));
        existing.setComment(instance.getComment());
        existing.setLastModifiedBy(user.getUuid());
        existing.setLastModifiedTime(Instant.now().toEpochMilli());

        //save the history
        WorkflowStateHistory historyEntry = WorkflowStateHistory.builder()
                .formInstanceId(existing.getFormInstanceId())
                .tenantId(existing.getTenantId())
                .fromState(existing.getCurrentState())
                .toState(matchedAction.getNextState())
                .action(instance.getAction())
                .comment(instance.getComment())
                .performedBy(user.getUuid())
                .performedAt(Instant.now().toEpochMilli())
                .build();

        workflowStateHistoryRepository.save(historyEntry);


        WorkflowInstance updated = workflowInstanceRepository.save(existing);
        return toResponse(updated);
    }

    public WorkflowInstanceResponse getByFormIdAndTenantId(String formId, String tenantId) {
        WorkflowInstance instance = workflowInstanceRepository
                .findByTenantIdAndFormInstanceId(tenantId, formId)
                .orElseThrow(() -> new WorkflowException("Workflow instance not found for form ID: " + formId));

        return toResponse(instance);
    }

    public List<WorkflowInstanceResponse> getInstancesByState(String tenantId, String currentState) {
        List<WorkflowInstance> instances =  workflowInstanceRepository.findByTenantIdAndCurrentState(tenantId, currentState);
        List<WorkflowInstanceResponse> responses = instances.stream()
                .map(this::toResponse)
                .toList();
        return responses;
    }

    public List<WorkflowInstanceResponse> getInstancesByStateAndService(String tenantId, String businessService, String currentState) {
        List<WorkflowInstance> instances =  workflowInstanceRepository.findByTenantIdAndBusinessServiceAndCurrentState(tenantId, businessService, currentState);
        List<WorkflowInstanceResponse> responses = instances.stream()
                .map(this::toResponse)
                .toList();
        return responses;
    }


    private WorkflowInstanceResponse toResponse(WorkflowInstance instance) {
        return WorkflowInstanceResponse.builder()
                .id(instance.getId())
                .formInstanceId(instance.getFormInstanceId())
                .businessService(instance.getBusinessService())
                .currentState(instance.getCurrentState())
                .previousStatus(instance.getPreviousStatus())
                .comment(instance.getComment())
                .assignees(instance.getAssignees())
                .createdBy(instance.getCreatedBy())
                .createdTime(instance.getCreatedTime())
                .lastModifiedBy(instance.getLastModifiedBy())
                .lastModifiedTime(instance.getLastModifiedTime())
                .tenantId(instance.getTenantId())
                .build();
    }

    public List<WorkflowHistoryResponse> getHistory(String formId, String tenantId) {
        List<WorkflowStateHistory> history = workflowStateHistoryRepository
                .findByFormInstanceIdAndTenantIdOrderByPerformedAtAsc(formId, tenantId);

        return history.stream().map(h -> WorkflowHistoryResponse.builder()
                        .fromState(h.getFromState())
                        .toState(h.getToState())
                        .action(h.getAction())
                        .comment(h.getComment())
                        .performedBy(h.getPerformedBy())
                        .performedAt(Instant.ofEpochMilli(h.getPerformedAt()).toString())
                        .build())
                .collect(Collectors.toList());
    }




}
