package com.strata.vms.vmsservice.entity;


import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@MappedSuperclass
public class BaseAuditedEntity {

    @CreationTimestamp
    @Column(name = "create_ts")
    protected Instant createTimestamp;

    @UpdateTimestamp
    @Column(name = "update_ts")
    protected Instant updateTimestamp;
}
