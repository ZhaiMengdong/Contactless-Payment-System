package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
* @Description:  表car，记录车辆信息
* @Author: ZMD
* @UpdateTime: 2019/8/17 11:28
*/
@Data
@TableName("car")
public class Car{

    //车辆vin号
    @TableId(value = "vin")
    private String vin;

    @TableField("accountId")
    private String accountId;

    //车牌号
    @TableField("license_plate")
    private String licensePlate;

    //车辆状态
    @TableField("status")
    private String status;


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
