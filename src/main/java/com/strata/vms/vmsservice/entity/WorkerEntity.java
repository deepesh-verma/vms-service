package com.strata.vms.vmsservice.entity;

import com.strata.vms.vmsservice.model.WorkerModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "worker")
public class WorkerEntity extends BaseAuditedEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "worker_role_id")
    private Long workerRoleId;

    @Column(name = "service_contract_id")
    private Long serviceContractId;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", insertable = false, updatable = false)
    private CompanyEntity company;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "worker_role_id", insertable = false, updatable = false)
    private WorkerRoleEntity workerRole;

    // As a worker can only be 100% assigned (full), at a time he can have only one service contract
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_contract_id", insertable = false, updatable = false)
    private ServiceContractEntity serviceContract;

    public WorkerEntity(Long id) {
        this.id = id;
    }

    public WorkerEntity(WorkerModel workerModel) {
        this.firstName = workerModel.getFirstName();
        this.lastName = workerModel.getLastName();
        this.startDate = workerModel.getStartDate();
        this.companyId = workerModel.getCompany().getId();
        this.workerRoleId = workerModel.getWorkerRole().getId();
        if (workerModel.getServiceContract() != null) {
            this.serviceContractId = workerModel.getServiceContract().getId();
        }
    }
}
