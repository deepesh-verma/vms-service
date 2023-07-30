package com.strata.vms.vmsservice.service;

import com.strata.vms.vmsservice.entity.ServiceContractEntity;
import com.strata.vms.vmsservice.entity.WorkerEntity;
import com.strata.vms.vmsservice.exception.AssignmentInvalidException;
import com.strata.vms.vmsservice.model.ServiceContractModel;
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
public class ServiceContractService {

    private final ServiceContractRepository contractRepository;

    private final WorkerRepository workerRepository;

    public ServiceContractService(@Autowired ServiceContractRepository contractRepository, @Autowired WorkerRepository workerRepository) {
        this.contractRepository = contractRepository;
        this.workerRepository = workerRepository;
    }

    public Long addServiceContract(ServiceContractModel contractModel) {
        log.debug("Adding contract: {}", contractModel);
        return contractRepository.save(new ServiceContractEntity(contractModel)).getId();
    }

    public void removeServiceContract(Long contractId) {
        log.debug("Removing contract: {}", contractId);
        contractRepository.deleteById(contractId);
    }

    public Page<ServiceContractModel> getAllContracts(Pageable pageable) {
        log.debug("Getting all contracts with pageable: {}", pageable);
        return contractRepository.findAll(pageable).map(ServiceContractModel::new);
    }

    public Optional<ServiceContractModel> getContractWithDetails(Long contractId) {
        log.debug("Getting contract: {}", contractId);
        return contractRepository.findWithDetailsById(contractId).map(ServiceContractModel::new);
    }

    public void assignOwner(Long workerId, Long contractId) {
        log.debug("Assigning worker as owner: {} for service contract: {}", workerId, contractId);

        this.validateIfCanBeAssigned(workerId, contractId);

        // Get contract entity proxy and set owner as the input worker
        ServiceContractEntity contractEntity = contractRepository.getReferenceById(contractId);
        WorkerEntity owner = new WorkerEntity(workerId);
        contractEntity.setOwner(owner);

        // Persist changes to the database
        contractRepository.save(contractEntity);
    }

    private void validateIfCanBeAssigned(Long workerId, Long contractId) {
        log.debug("Validating if worker with Id: {} can be assigned to the contractId: {}",workerId, contractId);

        List<Long> companyIdsByWorkerIds = workerRepository.findDistinctCompaniesByWorkerIds(List.of(workerId));
        if (CollectionUtils.isEmpty(companyIdsByWorkerIds)) {
            log.error("Worker: {} does not have a valid company assignment", workerId);
            throw new AssignmentInvalidException("Worker does not have a valid company assignment");
        }

        final Long contractCompanyId = contractRepository.getCompanyIdById(contractId);
        if (contractCompanyId == null) {
            log.error("Contract: {} does not have a valid company assignment", contractId);
            throw new AssignmentInvalidException("Contract does not have a valid company assignment");
        }

        final Long workerCompanyId = companyIdsByWorkerIds.get(0);
        if (!workerCompanyId.equals(contractCompanyId)) {
            log.error("Can't assign owner from a different company");
            throw new AssignmentInvalidException("Can't assign owner from a different company");
        }
    }
}
