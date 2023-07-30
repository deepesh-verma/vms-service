package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.ContractStatusEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class ContractStatusModel extends BaseAuditedModel {

    private Long id;

    @NotEmpty
    @Size(max = 64)
    private String name;

    @Size(max = 255)
    private String description;

    public ContractStatusModel(Long id) {
        this.id = id;
    }

    public ContractStatusModel(Long id, String name, String description, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public ContractStatusModel(ContractStatusEntity contractStatusEntity) {
        super(contractStatusEntity);
        this.id = contractStatusEntity.getId();
        this.name = contractStatusEntity.getName();
        this.description = contractStatusEntity.getDescription();
    }
}
