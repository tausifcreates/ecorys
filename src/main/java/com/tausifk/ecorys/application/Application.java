package com.tausifk.ecorys.application;

import com.tausifk.ecorys.common.ConflictException;
import com.tausifk.ecorys.worker.Worker;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "worker_id", nullable = false)
    private Worker worker;

    @NotNull
    @PastOrPresent
    @Column(nullable = false)
    private LocalDate applicationDate;

    @NotBlank
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String reason;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    private LocalDate approvalDate;

    protected Application() {
    }

    public Application(Worker worker, LocalDate applicationDate, String reason) {
        this.worker = worker;
        this.applicationDate = applicationDate;
        this.reason = reason;
    }

    public void approve(LocalDate approvalDate) {
        requirePending();
        this.status = ApplicationStatus.APPROVED;
        this.approvalDate = approvalDate;
    }

    public void reject() {
        requirePending();
        this.status = ApplicationStatus.REJECTED;
    }

    public boolean isPending() {
        return status == ApplicationStatus.SUBMITTED;
    }

    private void requirePending() {
        if (!isPending()) {
            throw new ConflictException("Application #" + id + " has already been " + status.getLabel().toLowerCase());
        }
    }

    public Long getId() {
        return id;
    }

    public Worker getWorker() {
        return worker;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public String getReason() {
        return reason;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public LocalDate getApprovalDate() {
        return approvalDate;
    }
}
