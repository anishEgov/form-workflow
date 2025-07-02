package com.example.formworkflow.repository;

import com.example.formworkflow.model.FormData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FormDataRepository extends JpaRepository<FormData, UUID> {
    Optional<FormData> findByBusinessServiceAndTenantId(String businessService, String tenantId);
}