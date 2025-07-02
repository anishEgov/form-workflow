package com.example.formworkflow.controller;

import com.example.formworkflow.dto.BusinessServiceRequest;
import com.example.formworkflow.dto.BusinessServiceResponse;
import com.example.formworkflow.dto.RequestInfo;
import com.example.formworkflow.service.BusinessServiceConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wf/businessservice")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class BusinessServiceController {

    private final BusinessServiceConfigService businessServiceConfigService;

    @PostMapping("/_create")
    public ResponseEntity<BusinessServiceResponse> createBusinessService(
            @Valid @RequestBody BusinessServiceRequest request
    ) {
        var created = businessServiceConfigService.create(request.getBusinessServices());

        return ResponseEntity.ok(
                BusinessServiceResponse.builder()
                        .responseInfo(Map.of("status", "successful"))
                        .businessServices(created)
                        .build()
        );
    }

    @PostMapping("/_search")
    public ResponseEntity<BusinessServiceResponse> searchBusinessService(
            @RequestParam String tenantId,
            @RequestParam(required = false) String businessServices,
            @Valid @RequestBody RequestInfo requestInfo
    ) {
        var results = businessServiceConfigService.search(tenantId, businessServices);

        return ResponseEntity.ok(
             
                BusinessServiceResponse.builder()
                        .responseInfo(Map.of("status", "successful"))
                        .businessServices(results)
                        .build()
        );
    }
}
