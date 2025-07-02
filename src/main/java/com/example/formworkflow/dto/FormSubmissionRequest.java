package com.example.formworkflow.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FormSubmissionRequest {
    private String formType;
    private String formInstanceId;
    private String businessService;
    private String tenantId;
    private Map<String,Object> data;
    private RequestInfo requestInfo;
}