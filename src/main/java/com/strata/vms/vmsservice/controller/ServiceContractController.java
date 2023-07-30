package com.strata.vms.vmsservice.controller;

import com.strata.vms.vmsservice.model.ServiceContractModel;
import com.strata.vms.vmsservice.service.ServiceContractService;
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
@RequestMapping("/contracts")
public class ServiceContractController {

    @Autowired
    private ServiceContractService serviceContractService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addServiceContract(@RequestBody @Validated ServiceContractModel serviceContractModel) {
        return serviceContractService.addServiceContract(serviceContractModel);
    }

    @GetMapping
    public Page<ServiceContractModel> getAllContracts(@RequestAttribute Pageable pageable) {
        return serviceContractService.getAllContracts(pageable);
    }

    @GetMapping(path = "/{id}")
    public ServiceContractModel getContractWithDetails(@PathVariable Long id) {
        
        Optional<ServiceContractModel> serviceContractWithDetails = serviceContractService.getContractWithDetails(id);
        if (serviceContractWithDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ServiceContract with id: " + id + "could not be found");
        }
        
        return serviceContractWithDetails.get();
    }
    
    @DeleteMapping("/{id}")
    public void removeContract(@PathVariable Long id) {
        serviceContractService.removeServiceContract(id);
    }

    @PatchMapping("/{id}/owner/{ownerId}")
    public void assignOwner(@PathVariable Long id, @PathVariable Long ownerId) {
        serviceContractService.assignOwner(ownerId, id);
    }
}
