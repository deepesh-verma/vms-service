package com.strata.vms.vmsservice.repository;

import com.strata.vms.vmsservice.entity.ServiceContractEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceContractRepository extends JpaRepository<ServiceContractEntity, Long> {

    @EntityGraph(attributePaths = {"status", "company", "owner"})
    Optional<ServiceContractEntity> findWithDetailsById(Long contractId);

    @Query("select c.id from service_contract s join s.company as c where s.id = :id")
    Long getCompanyIdById(Long id);
}
