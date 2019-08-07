package com.example.demo222.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created by 夏路遥
 * 8/1/2019 11:12 AM
 */
@Entity
public class ReqUser {

    private String institutionID;
    private String txCode;
    private String txSnBinding;
    private String bankID;
    private String accountName;
    private String accountNumber;
    private String identificationType;
    private String identificationNumber;
    private String phoneNumber;
    private String cardType;


    @Basic
    @Column(name = "institutionID")
    public String getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(String institutionID) {
        this.institutionID = institutionID;
    }

    @Basic
    @Column(name = "txCode")
    public String getTxCode() {
        return txCode;
    }

    public void setTxCode(String txCode) {
        txCode = txCode;
    }

    @Basic
    @Column(name = "txSnBinding")
    public String getTxSnBinding() {
        return txSnBinding;
    }

    public void setTxSnBinding(String txSnBinding) {
        txSnBinding = txSnBinding;
    }

    @Basic
    @Column(name = "bankID")
    public String getBankID() {
        return bankID;
    }

    public void setBankID(String bankID) {
        bankID = bankID;
    }

    @Basic
    @Column(name = "accountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Basic
    @Column(name = "accountNumber")
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Basic
    @Column(name = "identificationType")
    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    @Basic
    @Column(name = "identificationNumber")
    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    @Basic
    @Column(name = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Basic
    @Column(name = "cardType")
    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }


}
