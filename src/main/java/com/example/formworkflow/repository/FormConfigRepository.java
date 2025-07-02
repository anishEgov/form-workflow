package com.example.formworkflow.repository;

import com.example.formworkflow.model.FormConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface FormConfigRepository extends JpaRepository<FormConfig, UUID> {
    Optional<FormConfig> findByFormType(String formType);
    List<FormConfig> findByTenantId(String tenantId);
}
