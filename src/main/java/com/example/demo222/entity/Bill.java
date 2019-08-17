package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: zmd
 * @Date: 2019/8/12 19:08
 **/
@Data
@TableName("Bill")
public class Bill {

    @TableField("accountId")
    private String accountId;

    @TableField("time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    @TableField("amount")
    private String amount;

    @TableField("institutionId")
    private String institutionId;

    @TableField("cardNumber")
    private String cardNumber;

    @TableField("status")
    private String status;

    @TableField("paymentNumber")
    private String paymentNumber;

    @TableField("deviceNumber")
    private String deviceNumber;

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public Date getTime() {
        return time;
    }

    public String getAmount() {
        return amount;
    }

    public String getInstitutionId() {
        return institutionId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public String getPaymentNumber() {
        return paymentNumber;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setInstitutionId(String institutionId) {
        this.institutionId = institutionId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPaymentNumber(String paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
}
