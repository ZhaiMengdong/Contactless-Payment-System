package com.example.demo222.entity;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @Author: zmd
 * @Date: 2019/8/6 20:14
 **/
@Entity
public class Card {

    private String accountId;
    private String cardNumber;
    private String cardType;
    private String bankID;
    private String txSnBinding;
    private int status;

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

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTxSnBinding(String txSnBinding) {
        this.txSnBinding = txSnBinding;
    }

    @Basic
    @Column(name = "txSnBinding")
    public String getTxSnBinding() {
        return txSnBinding;
    }

    @Basic
    @Column(name = "accountId")
    public String getAccountId() {
        return accountId;
    }

    @Basic
    @Column(name = "cardNumber")
    public String getCardNumber() {
        return cardNumber;
    }

    @Basic
    @Column(name = "cardType")
    public String getCardType() {
        return cardType;
    }

    @Basic
    @Column(name = "bankID")
    public String getBankID() {
        return bankID;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }
}
