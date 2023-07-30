package com.strata.vms.vmsservice.service;

import com.strata.vms.vmsservice.entity.ContractStatusEntity;
import com.strata.vms.vmsservice.entity.WorkerRoleEntity;
import com.strata.vms.vmsservice.repository.ContractStatusRepository;
import com.strata.vms.vmsservice.repository.WorkerRoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class DatabaseInitService {

    @Autowired
    private WorkerRoleRepository workerRoleRepository;

    @Autowired
    private ContractStatusRepository contractStatusRepository;

    public void initMasterData() {

        this.initWorkerRoleMasterData();
        this.initContractStatusMasterData();
        log.info("Initialization of master tables data complete");
    }

    private void initContractStatusMasterData() {

        log.info("Initializing contract_status master table data");
        contractStatusRepository.saveAll(
                List.of(
                        new ContractStatusEntity("active", "Active status"),
                        new ContractStatusEntity("draft", "Draft status"),
                        new ContractStatusEntity("approved", "Approved status"),
                        new ContractStatusEntity("inactive", "Inactive status")
                )
        );
    }

    private void initWorkerRoleMasterData() {

        log.info("Initializing worker_role master table data");
        workerRoleRepository.saveAll(
                List.of(
                        new WorkerRoleEntity("contract_worker", "Contract worker role"),
                        new WorkerRoleEntity("contract_owner", "Contract owner role")
                )
        );
    }
}
