package com.strata.vms.vmsservice.entity;

import com.strata.vms.vmsservice.model.ContractStatusModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "contract_status")
public class ContractStatusEntity extends BaseAuditedEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    private String description;

    public ContractStatusEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ContractStatusEntity(ContractStatusModel contractStatusModel) {
        this.id = contractStatusModel.getId();
        this.name = contractStatusModel.getName();
        this.description = contractStatusModel.getDescription();
    }

    public boolean isActive() {
        return this.name.equals("active");
    }
}
