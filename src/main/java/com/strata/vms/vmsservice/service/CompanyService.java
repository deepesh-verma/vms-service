package com.strata.vms.vmsservice.service;

import com.strata.vms.vmsservice.entity.CompanyEntity;
import com.strata.vms.vmsservice.model.CompanyModel;
import com.strata.vms.vmsservice.model.CompanyWithDetailsModel;
import com.strata.vms.vmsservice.repository.CompanyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(@Autowired CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Long addCompany(CompanyModel companyModel) {
        log.debug("Adding company: {}", companyModel);
        return companyRepository.save(new CompanyEntity(companyModel)).getId();
    }

    public void removeCompany(Long companyId) {
        log.debug("Removing company: {}", companyId);
        companyRepository.deleteById(companyId);
    }

    public Page<CompanyModel> getAllCompanies(Pageable pageable) {
        log.debug("Getting all companies with pageable: {}", pageable);
        return companyRepository.findAll(pageable).map(CompanyModel::new);
    }

    public Optional<CompanyWithDetailsModel> getCompanyWithDetails(Long companyId) {
        log.debug("Getting company: {}", companyId);
        return companyRepository.findWithDetailsById(companyId).map(CompanyWithDetailsModel::new);
    }
}
