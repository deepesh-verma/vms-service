package com.strata.vms.vmsservice.model;

import com.strata.vms.vmsservice.entity.CompanyEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CompanyModel extends BaseAuditedModel {

    protected Long id;

    @NotEmpty
    @Size(max = 64)
    protected String name;

    @Size(max = 255)
    protected String description;

    @Size(max = 255)
    protected String address;

    @Size(max = 255)
    protected String phone;

    @Size(max = 255)
    @Email(regexp = ".+[@].+[\\.].+")
    protected String email;

    @Size(max = 255)
    protected String web;

    @Size(max = 255)
    protected String logo;

    public CompanyModel(Long id) {
        this.id = id;
    }

    public CompanyModel(Long id, String name, String description, String address, String phone, String email, String web, String logo, Instant createdAt, Instant updatedAt) {
        super(createdAt, updatedAt);
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.web = web;
        this.logo = logo;
    }

    public CompanyModel(CompanyEntity companyEntity) {
        super(companyEntity);
        this.id = companyEntity.getId();
        this.name = companyEntity.getName();
        this.description = companyEntity.getDescription();
        this.address = companyEntity.getAddress();
        this.phone = companyEntity.getPhone();
        this.email = companyEntity.getEmail();
        this.web = companyEntity.getWeb();
        this.logo = companyEntity.getLogo();
    }
}
