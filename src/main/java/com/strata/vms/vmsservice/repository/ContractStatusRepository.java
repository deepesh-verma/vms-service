package com.strata.vms.vmsservice.repository;

import com.strata.vms.vmsservice.entity.ContractStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractStatusRepository extends JpaRepository<ContractStatusEntity, Long> {
}
