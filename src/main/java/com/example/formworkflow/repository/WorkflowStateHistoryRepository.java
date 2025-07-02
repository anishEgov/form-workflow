package com.example.formworkflow.repository;

import com.example.formworkflow.model.WorkflowStateHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkflowStateHistoryRepository extends JpaRepository<WorkflowStateHistory, UUID> {
    List<WorkflowStateHistory> findByFormInstanceIdAndTenantIdOrderByPerformedAtAsc(String formInstanceId, String tenantId);
}
