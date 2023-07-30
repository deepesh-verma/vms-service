package com.strata.vms.vmsservice.service;

import com.strata.vms.vmsservice.entity.ServiceContractEntity;
import com.strata.vms.vmsservice.entity.WorkerEntity;
import com.strata.vms.vmsservice.exception.AssignmentInvalidException;
import com.strata.vms.vmsservice.model.WorkerModel;
import com.strata.vms.vmsservice.repository.ServiceContractRepository;
import com.strata.vms.vmsservice.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    private final ServiceContractRepository contractRepository;

    public WorkerService(@Autowired WorkerRepository workerRepository, @Autowired ServiceContractRepository contractRepository) {
        this.workerRepository = workerRepository;
        this.contractRepository = contractRepository;
    }

    public Long addWorker(WorkerModel workerModel) {
        log.debug("Adding worker: {}", workerModel);
        return workerRepository.save(new WorkerEntity(workerModel)).getId();
    }

    public void removeWorker(Long workerId) {
        log.debug("Removing worker: {}", workerId);
        workerRepository.deleteById(workerId);
    }

    public Page<WorkerModel> getAllWorkers(Pageable pageable) {
        log.debug("Getting all workers with pageable: {}", pageable);
        return workerRepository.findAll(pageable).map(WorkerModel::new);
    }

    public Optional<WorkerModel> getWorkerWithDetails(Long workerId) {
        log.debug("Getting worker: {}", workerId);
        return workerRepository.findWithDetailsById(workerId).map(WorkerModel::new);
    }

    public void offBoardFromContract(List<Long> workerIds) {
        log.debug("Off-boarding workers with ids: {} from contract(s)", workerIds);

        // Remove contractId from all these workers
        List<WorkerEntity> updatedWorkers = workerIds.stream()
                .map(workerRepository::getReferenceById)
                .map(entity -> {
                    entity.setServiceContract(null);
                    return entity;
                }).toList();

        // Persist the changes to database
        workerRepository.saveAll(updatedWorkers);
    }

    public void assignContract(List<Long> workerIds, Long contractId) {
        log.debug("Assigning contract: {} to the workers: {}", contractId, workerIds);

        this.validateIfCanBeAssigned(workerIds, contractId);

        // Set contractId to all these workers
        List<WorkerEntity> updatedWorkers = workerIds.stream()
                .map(workerRepository::getReferenceById)
                .map(entity -> {
                    entity.setServiceContractId(contractId);
                    return entity;
                }).toList();

        // Persist the changes to database
        workerRepository.saveAll(updatedWorkers);
    }

    private void validateIfCanBeAssigned(List<Long> workerIds, Long contractId) {
        log.debug("Validating if workerIds: {} can be assigned contractId: {}", workerIds, contractId);

        List<Long> companyIdsByWorkerIds = workerRepository.findDistinctCompaniesByWorkerIds(workerIds);
        if (CollectionUtils.isEmpty(companyIdsByWorkerIds)) {
            return;
        }

        // If more than one company is returned, then the assignment can't happen
        if (companyIdsByWorkerIds.size() > 1) {
            log.error("All workers: {} do not belong to the same company for contract assignment", workerIds);
            throw new AssignmentInvalidException("Please provide workers from the same company for contract assignment");
        }

        final Long existingCompanyId = companyIdsByWorkerIds.get(0);
        final Long newContractCompanyId = contractRepository.getCompanyIdById(contractId);
        if (existingCompanyId == null || newContractCompanyId == null) {
            log.error("The contract: {} does not have valid company assignment", contractId);
            throw new AssignmentInvalidException("The contract does not have valid company assignment");
        }

        if (!existingCompanyId.equals(newContractCompanyId)) {
            log.error("The worker can only be assigned to a contract from the same company");
            throw new AssignmentInvalidException("The worker can only be assigned to a contract from the same company");
        }

        final Optional<ServiceContractEntity> contractEntityOp = contractRepository.findWithDetailsById(contractId);
        if (contractEntityOp.isEmpty()) {
            log.error("Not a valid contractId for assignment");
            throw new AssignmentInvalidException("Please provide a valid contractId for workers assignment");
        }

        final ServiceContractEntity serviceContract = contractEntityOp.get();
        if (!serviceContract.getStatus().isActive()) {
            log.error("The contract is not in active status for assignment, current status is: " + serviceContract.getStatus().getName());
            throw new AssignmentInvalidException("The contract is not in active status for assignment");
        }
    }
}
