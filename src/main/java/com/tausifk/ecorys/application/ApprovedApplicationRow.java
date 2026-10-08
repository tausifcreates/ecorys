package com.tausifk.ecorys.application;

import java.time.LocalDate;

public record ApprovedApplicationRow(Long applicationId, String workerName, String nid, LocalDate applicationDate,
                                     LocalDate approvalDate, ApplicationStatus status) {
}
