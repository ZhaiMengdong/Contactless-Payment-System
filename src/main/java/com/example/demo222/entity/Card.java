package com.example.demo222.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @Author: zmd
 * @Date: 2019/8/6 20:14
 **/
//@Entity
//@Table(name = "card")
//public class Card {
//
//    @Id
//    private String txSnBinding;
//    private String accountId;
//    private String cardNumber;
//    private String cardType;
//    private String bankID;
//
//    private int status;
//
//    public void setAccountId(String accountId) {
//        this.accountId = accountId;
//    }
//
//    public void setCardNumber(String cardNumber) {
//        this.cardNumber = cardNumber;
//    }
//
//    public void setCardType(String cardType) {
//        this.cardType = cardType;
//    }
//
//    public void setBankID(String bankID) {
//        this.bankID = bankID;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//    }
//
//    public void setTxSnBinding(String txSnBinding) {
//        this.txSnBinding = txSnBinding;
//    }
//
//    @Column(name = "txSnBinding")
//    public String getTxSnBinding() {
//        return txSnBinding;
//    }
//
//    @Basic
//    @Column(name = "accountId")
//    public String getAccountId() {
//        return accountId;
//    }
//
//    @Basic
//    @Column(name = "cardNumber")
//    public String getCardNumber() {
//        return cardNumber;
//    }
//
//    @Basic
//    @Column(name = "cardType")
//    public String getCardType() {
//        return cardType;
//    }
//
//    @Basic
//    @Column(name = "bankID")
//    public String getBankID() {
//        return bankID;
//    }
//
//    @Basic
//    @Column(name = "status")
//    public int getStatus() {
//        return status;
//    }
//}
@Data
@TableName(value = "card")
public class Card{

//    @TableId(value = "txSnBinding",type = IdType.INPUT)
    @TableId(value = "txSnBinding")
    private String txSnBinding;

    @TableField("accountId")
    private String accountId;

    @TableField("cardNumber")
    private String cardNumber;

    @TableField("cardType")
    private String cardType;

    @TableField("bankID")
    private String bankID;

    @TableField("priority")
    private int priority;

    @TableField("tailNumber")
    private String tailNumber;

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
