package com.tausifk.ecorys.worker;

import com.tausifk.ecorys.common.validation.MobileNumber;
import com.tausifk.ecorys.common.validation.Nid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Nid
    @Column(nullable = false, unique = true, length = 17)
    private String nid;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank
    @MobileNumber
    @Column(nullable = false, length = 11)
    private String mobileNumber;

    protected Worker() {
    }

    public Worker(String nid, String name, String mobileNumber) {
        this.nid = nid;
        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public boolean matches(String name, String mobileNumber) {
        return this.name.equalsIgnoreCase(name) && this.mobileNumber.equals(mobileNumber);
    }

    public Long getId() {
        return id;
    }

    public String getNid() {
        return nid;
    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
