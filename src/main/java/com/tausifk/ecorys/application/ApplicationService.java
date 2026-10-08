package com.tausifk.ecorys.application;

import com.tausifk.ecorys.common.ConflictException;
import com.tausifk.ecorys.common.NotFoundException;
import com.tausifk.ecorys.worker.Worker;
import com.tausifk.ecorys.worker.WorkerRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ApplicationService {

    private final ApplicationRepository applications;
    private final WorkerRepository workers;

    public ApplicationService(ApplicationRepository applications, WorkerRepository workers) {
        this.applications = applications;
        this.workers = workers;
    }

    @Transactional
    public Application submit(ApplicationForm form) {
        Worker worker = findOrRegisterWorker(form.getNid(), form.getName().trim(), form.getMobileNumber());
        if (applications.existsByWorkerIdAndStatus(worker.getId(), ApplicationStatus.SUBMITTED)) {
            throw new ConflictException("This worker already has an application awaiting review");
        }
        return applications.save(new Application(worker, form.getApplicationDate(), form.getReason().trim()));
    }

    public List<Application> findAll() {
        return applications.findAllByOrderByApplicationDateDescIdDesc();
    }

    public List<ApprovedApplicationRow> approvedReport(Sort.Direction direction) {
        return applications.findApprovedReport(Sort.by(direction, "approvalDate", "id"));
    }

    @Transactional
    public void approve(Long id) {
        findById(id).approve(LocalDate.now());
    }

    @Transactional
    public void reject(Long id) {
        findById(id).reject();
    }

    private Worker findOrRegisterWorker(String nid, String name, String mobileNumber) {
        Worker worker = workers.findByNid(nid)
                .orElseGet(() -> workers.save(new Worker(nid, name, mobileNumber)));
        if (!worker.matches(name, mobileNumber)) {
            throw new ConflictException("This NID is registered with a different name or mobile number");
        }
        return worker;
    }

    private Application findById(Long id) {
        return applications.findById(id)
                .orElseThrow(() -> new NotFoundException("Application #" + id + " not found"));
    }
}
