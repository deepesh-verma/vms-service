package com.strata.vms.vmsservice.entity;

import com.strata.vms.vmsservice.model.WorkerRoleModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "worker_role")
public class WorkerRoleEntity extends BaseAuditedEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    private String description;

    public WorkerRoleEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public WorkerRoleEntity(WorkerRoleModel workerRole) {
        this.id = workerRole.getId();
        this.name = workerRole.getName();
        this.description = workerRole.getDescription();
    }
}
