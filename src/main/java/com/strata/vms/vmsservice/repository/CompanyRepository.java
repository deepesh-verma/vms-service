package com.strata.vms.vmsservice.repository;

import com.strata.vms.vmsservice.entity.CompanyEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {

    @EntityGraph(attributePaths = "workers")
    Optional<CompanyEntity> findWithDetailsById(Long companyId);
}
