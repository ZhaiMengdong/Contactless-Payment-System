package com.example.demo222.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
* @Description: 表card，记录银行卡信息
* @Author: ZMD
* @UpdateTime: 2019/8/17 11:31
*/
@Data
@TableName(value = "card")
public class Card{

    //绑定流水号
    @TableId(value = "txSnBinding")
    private String txSnBinding;

    @TableField("accountId")
    private String accountId;

    //卡号
    @TableField("cardNumber")
    private String cardNumber;

    //卡类型
    @TableField("cardType")
    private String cardType;

    //银行id
    @TableField("bankID")
    private String bankID;

    //支付优先级
    @TableField("priority")
    private int priority;

    //银行卡尾号
    @TableField("tailNumber")
    private String tailNumber;

    //银行卡状态
    @TableField("status")
    private int status;

    public int getStatus() {
        return status;
    }

    public String getTxSnBinding() {
        return txSnBinding;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public String getBankID() {
        return bankID;
    }

    public int getPriority() {
        return priority;
    }

    public String getTailNumber() {
        return tailNumber;
    }

    public void setTxSnBinding(String txSnBinding) {
        this.txSnBinding = txSnBinding;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setBankID(String bankID) {
        this.bankID = bankID;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setTailNumber(String tailNumber) {
        this.tailNumber = tailNumber;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
