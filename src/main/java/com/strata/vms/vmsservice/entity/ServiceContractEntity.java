package com.strata.vms.vmsservice.entity;

import com.strata.vms.vmsservice.model.ServiceContractModel;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity(name = "service_contract")
public class ServiceContractEntity extends BaseAuditedEntity {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    private String description;


    @Column(name = "status_id", nullable = false)
    private Long statusId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @ToString.Exclude
    @ManyToOne(optional = false)
    @JoinColumn(name = "status_id", insertable = false, updatable = false)
    private ContractStatusEntity status;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;

    @ToString.Exclude
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private WorkerEntity owner;

    public ServiceContractEntity(ServiceContractModel serviceContractModel) {
        this.id = serviceContractModel.getId();
        this.name = serviceContractModel.getName();
        this.description = serviceContractModel.getDescription();
        this.statusId = serviceContractModel.getStatus().getId();
        this.companyId = serviceContractModel.getCompany().getId();
        this.ownerId = serviceContractModel.getOwner().getId();
    }

    public ServiceContractEntity(Long id) {
        this.id = id;
    }
}
