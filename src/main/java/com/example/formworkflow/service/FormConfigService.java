package com.example.formworkflow.service;

import com.example.formworkflow.dto.FormConfigRequest;
import com.example.formworkflow.dto.FormConfigResponse;
import com.example.formworkflow.model.FormConfig;
import com.example.formworkflow.repository.FormConfigRepository;
import com.example.formworkflow.validator.FormConfigValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FormConfigService {

    private final FormConfigRepository repository;
    private final FormConfigValidator validator;

    public FormConfigResponse create(FormConfigRequest request, String createdBy) {
        validator.validate(request);

        FormConfig config = FormConfig.builder()
//                .id(UUID.randomUUID())
                .formType(request.formType())
                .schema(request.schema())
                .tenantId(request.tenantId())
                .createdBy(createdBy)
                .createdTime(Instant.now())
                .build();

        return toResponse(repository.save(config));
    }

    public Optional<FormConfigResponse> getByFormType(String formType) {
        return repository.findByFormType(formType).map(this::toResponse);
    }

    public List<FormConfigResponse> getByTenantId(String tenantId) {
        return repository.findByTenantId(tenantId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private FormConfigResponse toResponse(FormConfig config) {
        return new FormConfigResponse(
                config.getId(),
                config.getFormType(),
                config.getSchema(),
                config.getTenantId(),
                config.getCreatedBy(),
                config.getCreatedTime()
        );
    }
}
