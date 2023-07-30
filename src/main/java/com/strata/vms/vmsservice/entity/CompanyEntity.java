package com.strata.vms.vmsservice.entity;

import com.strata.vms.vmsservice.model.CompanyModel;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity(name = "company")
public class CompanyEntity extends BaseAuditedEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 64, nullable = false)
    private String name;

    private String description;

    private String address;

    private String phone;

    private String email;

    private String web;

    private String logo;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "company")
    private List<WorkerEntity> workers;

    public CompanyEntity(CompanyModel companyModel) {
        this.name = companyModel.getName();
        this.description = companyModel.getDescription();
        this.address = companyModel.getAddress();
        this.phone = companyModel.getPhone();
        this.email = companyModel.getEmail();
        this.web = companyModel.getWeb();
        this.logo = companyModel.getLogo();
    }
}
