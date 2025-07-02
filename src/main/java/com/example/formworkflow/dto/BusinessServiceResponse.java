package com.example.formworkflow.dto;

import com.example.formworkflow.model.BusinessService;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class BusinessServiceResponse {
    private Map<String, Object> responseInfo;
    private List<BusinessService> businessServices;
}
