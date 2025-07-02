package com.example.formworkflow.service;

import com.example.formworkflow.exception.WorkflowException;
import com.example.formworkflow.model.ActionDefinition;
import com.example.formworkflow.model.BusinessService;
import com.example.formworkflow.model.StateDefinition;
import com.example.formworkflow.repository.BusinessServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BusinessServiceConfigService {

    private final BusinessServiceRepository repository;

    public List<BusinessService> create(List<BusinessService> services) {
        if (services == null || services.isEmpty()) {
            throw new WorkflowException("At least one BusinessService is required");
        }

        for (BusinessService bs : services) {
            if (!StringUtils.hasText(bs.getBusinessService())) {
                throw new WorkflowException("businessService name is required");
            }
            if (!StringUtils.hasText(bs.getTenantId())) {
                throw new WorkflowException("tenantId is required for businessService: " + bs.getBusinessService());
            }

            List<StateDefinition> states = bs.getStates();
            if (states == null || states.isEmpty()) {
                throw new WorkflowException("At least one state is required for businessService: " + bs.getBusinessService());
            }

            // Enforce exactly one start state
            long startStateCount = states.stream().filter(state -> Boolean.TRUE.equals(state.getIsStartState())).count();
            if (startStateCount != 1) {
                throw new WorkflowException("Exactly one start state must be defined for businessService: " + bs.getBusinessService());
            }

            // Collect all valid state names for reference checks
            Set<String> stateNames = new HashSet<>();
            for (StateDefinition state : states) {
                if (!StringUtils.hasText(state.getState())) {
                    throw new WorkflowException("All states must have a valid 'state' name in businessService: " + bs.getBusinessService());
                }
                stateNames.add(state.getState());
            }

            // Set reverse reference and validate actions
            for (StateDefinition state : states) {
                state.setBusinessService(bs); // owning side

                if (state.getActions() != null) {
                    for (ActionDefinition action : state.getActions()) {
                        if (!StringUtils.hasText(action.getAction()) || !StringUtils.hasText(action.getNextState())) {
                            throw new WorkflowException("Action and nextState must be provided in all transitions of state: " + state.getState());
                        }

                        // Check nextState is a valid state
                        if (!stateNames.contains(action.getNextState())) {
                            throw new WorkflowException("Invalid nextState '" + action.getNextState() + "' in action '" + action.getAction() +
                                    "' of state '" + state.getState() + "'. It must match one of the defined state names.");
                        }

                        action.setState(state); // owning side
                    }
                }
            }
        }

        return repository.saveAll(services);
    }

    public List<BusinessService> search(String tenantId, String businessServiceName) {
        if (!StringUtils.hasText(tenantId)) {
            throw new WorkflowException("tenantId is required for search");
        }

        if (StringUtils.hasText(businessServiceName)) {
            return repository.findByTenantIdAndBusinessService(tenantId, businessServiceName);
        } else {
            return repository.findByTenantId(tenantId);
        }
    }
}
