package com.strata.vms.vmsservice.controller;

import com.strata.vms.vmsservice.model.WorkerModel;
import com.strata.vms.vmsservice.service.WorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/workers")
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long addWorker(@RequestBody @Validated WorkerModel workerModel) {
        return workerService.addWorker(workerModel);
    }

    @GetMapping
    public Page<WorkerModel> getAllWorkers(@RequestAttribute Pageable pageable) {
        return workerService.getAllWorkers(pageable);
    }

    @GetMapping(path = "/{id}")
    public WorkerModel getWorkerWithDetails(@PathVariable Long id) {
        
        Optional<WorkerModel> workerWithDetails = workerService.getWorkerWithDetails(id);
        if (workerWithDetails.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Worker with id: " + id + "could not be found");
        }
        
        return workerWithDetails.get();
    }
    
    @DeleteMapping("/{id}")
    public void removeWorker(@PathVariable Long id) {
        workerService.removeWorker(id);
    }

    @PatchMapping("/assign/{contractId}")
    public void assignContract(@RequestBody List<Long> workerIds, @PathVariable Long contractId) {
        workerService.assignContract(workerIds,contractId);
    }

    @PatchMapping("/offboard")
    public void offBoardWorkers(@RequestBody List<Long> workerIds) {
        workerService.offBoardFromContract(workerIds);
    }
}
