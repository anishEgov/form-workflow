package com.example.formworkflow.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workflow_instance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkflowInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String formInstanceId;

    @Column(nullable = false)
    private String businessService; // Which workflow config this instance follows

    @Column(nullable = false)
    private String currentState; // Current state in the workflow

    @Column(nullable = false)
    private String tenantId; // For multi-tenant support

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "workflow_assignees", joinColumns = @JoinColumn(name = "instance_id"))
    @Column(name = "assignee")
    private List<String> assignees; // Who's responsible next

    private String createdBy;
    private Long createdTime;
    private String lastModifiedBy;
    private Long lastModifiedTime;

    // NEW FIELDS added based on your example
    private String moduleName;        // Optional field for tracking source module
    private String comment;           // Any comments added during transition
    private String previousStatus;    // Stores status before transition (for trace/audit)
}


