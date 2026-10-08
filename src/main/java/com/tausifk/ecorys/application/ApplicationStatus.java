package com.tausifk.ecorys.application;

public enum ApplicationStatus {
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
