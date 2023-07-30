package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.WorkerRoleEntity;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
public class WorkerRoleModel extends BaseAuditedModel {

    private Long id;

    @NotEmpty
    @Size(max = 64)
    private String name;

    @Size(max = 64)
    private String description;

    public WorkerRoleModel(Long id) {
        this.id = id;
    }

    public WorkerRoleModel(Long id, String name, String description, Instant createdAt, Instant updateAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
        this.updatedAt = updateAt;
    }

    public WorkerRoleModel(WorkerRoleEntity workerRoleEntity) {
        super(workerRoleEntity);
        this.id = workerRoleEntity.getId();
        this.name = workerRoleEntity.getName();
        this.description = workerRoleEntity.getDescription();
    }
}
