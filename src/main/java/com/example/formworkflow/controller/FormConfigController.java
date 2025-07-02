package com.example.formworkflow.controller;

import com.example.formworkflow.dto.FormConfigRequest;
import com.example.formworkflow.dto.FormConfigResponse;
import com.example.formworkflow.service.FormConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forms/config")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class FormConfigController {

    private final FormConfigService formConfigService;

    @PostMapping
    public ResponseEntity<FormConfigResponse> create(@RequestBody FormConfigRequest request,
                                                     @RequestHeader("X-User-Id") String userId) {
        return ResponseEntity.ok(formConfigService.create(request, userId));
    }

    @GetMapping("/{formType}")
    public ResponseEntity<FormConfigResponse> getByFormType(@PathVariable String formType) {
        return formConfigService.getByFormType(formType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<FormConfigResponse>> getByTenantId(@RequestParam String tenantId) {
        return ResponseEntity.ok(formConfigService.getByTenantId(tenantId));
    }
}
