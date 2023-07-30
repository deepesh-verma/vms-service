package com.strata.vms.vmsservice.repository;


import com.strata.vms.vmsservice.entity.WorkerRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkerRoleRepository extends JpaRepository<WorkerRoleEntity, Long> {
}
