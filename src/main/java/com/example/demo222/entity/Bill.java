package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** 
* @Description: 表bill，记录订单信息
* @Author: ZMD
* @UpdateTime: 2019/8/17 11:17
*/
@Data
@TableName("Bill")
public class Bill {

    @TableField("accountId")
    private String accountId;

    //订单生成的时间
    @TableField("time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    //交易金额
    @TableField("amount")
    private String amount;

    //机构号码
    @TableField("institutionId")
    private String institutionId;

    //支付所用的银行卡卡号
    @TableField("cardNumber")
    private String cardNumber;

    //订单状态
    @TableField("status")
    private String status;

    //订单流水号
    @TableField("paymentNumber")
    private String paymentNumber;

    //充电桩编号
    @TableField("deviceNumber")
    private String deviceNumber;

    private String carNumber;

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
