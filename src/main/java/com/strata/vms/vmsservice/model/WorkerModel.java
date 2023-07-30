package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.WorkerEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WorkerModel extends BaseAuditedModel {

    private Long id;

    @NotEmpty
    @Size(max = 255)
    private String firstName;

    @NotEmpty
    @Size(max = 255)
    private String lastName;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private CompanyModel company;

    @NotNull
    private WorkerRoleModel workerRole;

    private ServiceContractModel serviceContract;

    public WorkerModel(Long id) {
        this.id = id;
    }

    public WorkerModel(WorkerEntity workerEntity) {
        super(workerEntity);
        this.id = workerRole.getId();
        this.firstName = workerEntity.getFirstName();
        this.lastName = workerEntity.getLastName();
        this.startDate = workerEntity.getStartDate();
        this.company = new CompanyModel(workerEntity.getCompany());
        this.workerRole = new WorkerRoleModel(workerEntity.getWorkerRole());
        this.serviceContract = new ServiceContractModel(workerEntity.getServiceContract());
    }
}
