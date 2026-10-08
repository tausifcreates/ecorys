package com.tausifk.ecorys.application;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByWorkerIdAndStatus(Long workerId, ApplicationStatus status);

    @EntityGraph(attributePaths = "worker")
    List<Application> findAllByOrderByApplicationDateDescIdDesc();

    @Query("""
            select new com.tausifk.ecorys.application.ApprovedApplicationRow(
                a.id, w.name, w.nid, a.applicationDate, a.approvalDate, a.status)
            from Application a join a.worker w
            where a.status = com.tausifk.ecorys.application.ApplicationStatus.APPROVED
            """)
    List<ApprovedApplicationRow> findApprovedReport(Sort sort);
}
