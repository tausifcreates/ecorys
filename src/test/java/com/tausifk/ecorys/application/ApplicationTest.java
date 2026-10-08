package com.tausifk.ecorys.application;

import com.tausifk.ecorys.common.ConflictException;
import com.tausifk.ecorys.worker.Worker;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ApplicationTest {

    private final Application application = new Application(new Worker("1234567890", "Rahim", "01712345678"), LocalDate.of(2026, 10, 1), "Factory closed");

    @Test
    void newApplicationIsSubmitted() {
        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.SUBMITTED);
        assertThat(application.isPending()).isTrue();
    }

    @Test
    void approveRecordsApprovalDate() {
        application.approve(LocalDate.of(2026, 10, 8));

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.APPROVED);
        assertThat(application.getApprovalDate()).isEqualTo(LocalDate.of(2026, 10, 8));
    }

    @Test
    void rejectLeavesApprovalDateEmpty() {
        application.reject();

        assertThat(application.getStatus()).isEqualTo(ApplicationStatus.REJECTED);
        assertThat(application.getApprovalDate()).isNull();
    }

    @Test
    void decidedApplicationCannotBeDecidedAgain() {
        application.reject();

        assertThatThrownBy(() -> application.approve(LocalDate.now())).isInstanceOf(ConflictException.class);
        assertThatThrownBy(application::reject).isInstanceOf(ConflictException.class);
    }
}
