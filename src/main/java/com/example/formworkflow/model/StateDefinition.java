package com.example.formworkflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "state_definition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StateDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String state;

    @Column(name = "is_start_state")
    private Boolean isStartState;

    @Column(name = "is_terminate_state")
    private Boolean isTerminateState;

    @Column(name = "is_state_updatable")
    private Boolean isStateUpdatable;

    private String applicationStatus;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_service_id")
    @JsonIgnore
    private BusinessService businessService;

    @OneToMany(mappedBy = "state", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActionDefinition> actions;
}
