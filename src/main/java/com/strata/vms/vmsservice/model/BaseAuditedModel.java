package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.BaseAuditedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseAuditedModel {

    protected Instant createdAt;
    protected Instant updatedAt;

    protected BaseAuditedModel(BaseAuditedEntity baseAuditedEntity) {
        this.createdAt = baseAuditedEntity.getCreateTimestamp();
        this.updatedAt = baseAuditedEntity.getUpdateTimestamp();
    }
}
