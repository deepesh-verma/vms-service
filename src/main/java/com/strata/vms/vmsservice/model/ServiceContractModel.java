package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.ServiceContractEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ServiceContractModel extends BaseAuditedModel {

    private Long id;

    @NotEmpty
    @Size(max = 64)
    private String name;

    @Size(max = 255)
    private String description;

    @NotNull
    private ContractStatusModel status;

    @NotNull
    private CompanyModel company;

    @NotNull
    private WorkerModel owner;

    public ServiceContractModel(Instant createdAt, Instant updatedAt, Long id, String name, String description,
                                @NotNull ContractStatusModel status, @NotNull CompanyModel company, @NotNull WorkerModel owner) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.company = company;
        this.owner = owner;
    }

    public ServiceContractModel(ServiceContractEntity serviceContractEntity) {
        super(serviceContractEntity);
        this.id = serviceContractEntity.getId();
        this.name = serviceContractEntity.getName();
        this.description = serviceContractEntity.getDescription();
        this.status = new ContractStatusModel(serviceContractEntity.getStatus());
        this.company = new CompanyModel(serviceContractEntity.getCompany());
        this.owner = new WorkerModel(serviceContractEntity.getOwner());
    }
}
