package com.example.formworkflow.repository;

import com.example.formworkflow.model.BusinessService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusinessServiceRepository extends JpaRepository<BusinessService, UUID> {

    Optional<BusinessService> findByBusinessServiceAndTenantId(String businessService, String tenantId);

    List<BusinessService> findByTenantId(String tenantId);

    List<BusinessService> findByTenantIdAndBusinessService(String tenantId, String businessService);
}
