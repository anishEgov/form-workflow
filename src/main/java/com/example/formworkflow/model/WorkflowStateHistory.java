package com.example.formworkflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "workflow_state_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowStateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String formInstanceId;

    @Column(nullable = false)
    private String tenantId;

    private String fromState;
    private String toState;
    private String action;
    private String comment;

    private String performedBy;
    private Long performedAt; // epoch millis
}
