package com.strata.vms.vmsservice.repository;

import com.strata.vms.vmsservice.entity.WorkerEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerEntity, Long> {

    @EntityGraph(attributePaths = {"company", "workerRole", "serviceContract"})
    Optional<WorkerEntity> findWithDetailsById(Long workerId);

    @Query("select distinct w.company.id from worker w where w.id in :workerIds")
    List<Long> findDistinctCompaniesByWorkerIds(List<Long> workerIds);
}
