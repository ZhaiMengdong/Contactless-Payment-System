package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;



/**
 * @Author: zmd
 * @Date: 2019/8/6 10:47
 **/
//@Entity
//@Table(name = "car")
//public class Car {
//
//    @Id
//    private String vin;
//    private String accountId;
//    private String license_plate;
//    private String status;
//
//    @Basic
//    @Column(name = "accountId")
//    public String getAccountId() {
//        return accountId;
//    }
//
//    @Column(name = "vin")
//    public String getVin() {
//        return vin;
//    }
//
//    @Basic
//    @Column(name = "license_plate")
//    public String getLicense_plate() {
//        return license_plate;
//    }
//
//    @Basic
//    @Column(name = "status")
//    public String getStatus() {
//        return status;
//    }
//
//
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }
//
//    public void setVin(String vin) {
//        this.vin = vin;
//    }
//
//    public void setLicense_plate(String license_plate) {
//        this.license_plate = license_plate;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//}
@Data
@TableName("car")
public class Car{

//    private static final long serialVersionUID = 1L;

//    @TableId(value = "vin", type = IdType.INPUT)
    @TableId(value = "vin")
    private String vin;

    @TableField("accountId")
    private String accountId;

    @TableField("license_plate")
    private String licensePlate;

    @TableField("status")
    private String status;

//    public static long getSerialVersionUID() {
//        return serialVersionUID;
//    }

    public String getVin() {
        return vin;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getStatus() {
        return status;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
