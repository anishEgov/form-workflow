package com.example.formworkflow.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "business_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tenantId;

    @Column(nullable = false ,unique = true)
    private String businessService;

    @Column(nullable = false)
    private String business; // module name (e.g., "form-workflow")

    private Long businessServiceSla;

    @OneToMany(mappedBy = "businessService", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<StateDefinition> states;

}

