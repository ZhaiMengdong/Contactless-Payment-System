package com.example.demo222.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @Author: zmd
 * @Date: 2019/8/6 10:47
 **/
@Entity
public class Car {
    private String accountId;
    private String vin;
    private String license_plate;
    private String status;

    @Basic
    @Column(name = "accountId")
    public String getAccountId() {
        return accountId;
    }

    @Basic
    @Column(name = "vin")
    public String getVin() {
        return vin;
    }

    @Basic
    @Column(name = "license_plate")
    public String getLicense_plate() {
        return license_plate;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }


    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setLicense_plate(String license_plate) {
        this.license_plate = license_plate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
