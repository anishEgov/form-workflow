package com.example.formworkflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "action_definition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String nextState;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "action_roles", joinColumns = @JoinColumn(name = "action_id"))
    @Column(name = "role")
    private List<String> roles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "state_id")
    @JsonIgnore
    private StateDefinition state;
}
