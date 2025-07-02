package com.example.formworkflow.service;

import com.example.formworkflow.dto.FormSubmissionRequest;
import com.example.formworkflow.dto.FormSubmissionResponse;
import com.example.formworkflow.dto.UserInfo;
import com.example.formworkflow.model.*;
import com.example.formworkflow.repository.*;
import com.example.formworkflow.validator.FormSchemaValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FormSubmissionService {

    private final FormDataRepository formDataRepository;
    private final WorkflowInstanceRepository workflowInstanceRepository;
    private final BusinessServiceRepository businessServiceRepository;
    private final FormConfigRepository formConfigRepository;
    private final FormSchemaValidator formSchemaValidator;

    public FormSubmissionResponse submitForm(FormSubmissionRequest request) {
        UserInfo user = request.getRequestInfo().getUserInfo();

        // Load and validate schema
        FormConfig formConfig = formConfigRepository.findByFormType(request.getFormType())
                .orElseThrow(() -> new RuntimeException("Form config not found for: " + request.getFormType()));
        formSchemaValidator.validate(formConfig.getSchema(), request.getData());

        // Load workflow config
        BusinessService businessServiceConfig = businessServiceRepository.findByBusinessServiceAndTenantId(
                request.getBusinessService(), request.getTenantId()
        ).orElseThrow(() -> new RuntimeException("No business service config found"));

        StateDefinition start = businessServiceConfig.getStates().stream()
                .filter(state -> Boolean.TRUE.equals(state.getIsStartState()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No start state defined"));


        // Save form data
        FormData formData = FormData.builder()
                .formType(request.getFormType())
                .formInstanceId(request.getFormInstanceId())
                .businessService(request.getBusinessService())
                .tenantId(request.getTenantId())
                .data(request.getData())
                .submittedBy(user.getUuid())
                .submittedAt(System.currentTimeMillis())
                .build();

        formDataRepository.save(formData);


        // Create workflow instance
        WorkflowInstance instance = WorkflowInstance.builder()
                .formInstanceId(formData.getId().toString()) // << Make sure this field exists in model
                .businessService(request.getBusinessService())
                .tenantId(request.getTenantId())
                .currentState(start.getState())
                .createdBy(user.getUuid())
                .createdTime(System.currentTimeMillis())
                .build();

        workflowInstanceRepository.save(instance);



        return FormSubmissionResponse.builder()
                .formId(formData.getId())
                .workflowInstanceId(instance.getId())
                .status("submitted")
                .currentState(start.getState())
                .submittedBy(user.getUuid())
                .submittedAt(formData.getSubmittedAt())
                .build();
    }


}