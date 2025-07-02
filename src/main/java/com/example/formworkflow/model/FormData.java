package com.example.formworkflow.model;

import com.example.formworkflow.utility.JsonConverter;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "form_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String formType;

    @Column(nullable = false, unique = true)
    private String formInstanceId;

    @Column(nullable = false)
    private String businessService;

    @Column(nullable = false)
    private String tenantId;

    @Column(columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> data;

    private String submittedBy;
    private Long submittedAt;
}


