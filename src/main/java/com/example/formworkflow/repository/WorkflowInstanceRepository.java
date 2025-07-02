package com.example.formworkflow.repository;

import com.example.formworkflow.model.WorkflowInstance;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, UUID> {

    Optional<WorkflowInstance> findByTenantIdAndBusinessService(
            String tenantId, String businessService
    );

    List<WorkflowInstance> findByTenantIdAndCurrentState(String tenantId, String currentState);
    List<WorkflowInstance> findByTenantIdAndBusinessServiceAndCurrentState(String tenantId, String businessService, String currentState);


    Optional<WorkflowInstance> findByTenantIdAndFormInstanceId(@NotBlank String tenantId, @NotBlank String formInstanceId);
}
