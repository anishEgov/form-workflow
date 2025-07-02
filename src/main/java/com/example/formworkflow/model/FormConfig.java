package com.example.formworkflow.model;

import com.example.formworkflow.utility.JsonConverter;
import jakarta.persistence.*;
        import lombok.*;
        import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "form_config")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "form_type", nullable = false, unique = true)
    private String formType;

    @Column(name = "schema", columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> schema;


    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "tenant_id")
    private String tenantId;
}




