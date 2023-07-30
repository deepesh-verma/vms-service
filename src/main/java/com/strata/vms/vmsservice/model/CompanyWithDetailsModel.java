package com.strata.vms.vmsservice.model;


import com.strata.vms.vmsservice.entity.CompanyEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
public class CompanyWithDetailsModel extends CompanyModel {

    private List<WorkerModel> workers;

    public CompanyWithDetailsModel(Long id, String name, String description, String address, String phone, String email, String web, String logo, Instant createdAt, Instant updatedAt, List<WorkerModel> workers) {
        super(id, name, description, address, phone, email, web, logo, createdAt, updatedAt);
        this.workers = workers;
    }

    public CompanyWithDetailsModel(CompanyEntity companyEntity) {
        super(companyEntity);
        this.workers = companyEntity.getWorkers().stream().map(WorkerModel::new).collect(Collectors.toList());
    }
}
