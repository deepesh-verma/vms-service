package com.strata.vms.vmsservice.controller;

import com.strata.vms.vmsservice.model.CompanyModel;
import com.strata.vms.vmsservice.model.CompanyWithDetailsModel;
import com.strata.vms.vmsservice.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addCompany(@RequestBody @Validated CompanyModel companyModel) {
        return companyService.addCompany(companyModel);
    }

    @GetMapping
    public Page<CompanyModel> getAllCompanies(@RequestAttribute Pageable pageable) {
        return companyService.getAllCompanies(pageable);
    }

    @GetMapping(path = "/{id}")
    public CompanyModel getCompanyWithDetails(@PathVariable Long id) {

        Optional<CompanyWithDetailsModel> companyWithDetails = companyService.getCompanyWithDetails(id);
        if (companyWithDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company with id: " + id + "could not be found");
        }

        return companyWithDetails.get();
    }

    @DeleteMapping("/{id}")
    public void removeCompany(@PathVariable Long id) {
        companyService.removeCompany(id);
    }
}
