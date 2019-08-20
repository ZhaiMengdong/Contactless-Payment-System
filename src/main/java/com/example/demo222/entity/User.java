package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/** 
* @Description: 表user，记录用户信息 
* @Author: ZMD
* @UpdateTime: 2019/8/17 13:43
*/ 
@Data
@TableName("user")
public class User {
    private static final long serialVersionUID = 1L;
    /**
     * accountId
     */
//    @TableId(value = "accountId",type = IdType.INPUT)
    @TableId(value = "accountId")
    private String accountId;
    /**
     * skey
     */
    @TableField("skey")
    private String skey;
    /**
     * 创建时间
     */
    @TableField("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /**
     * 最后登录时间
     */
    @TableField("last_visit_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date lastVisitTime;
    /**
     * session_key
     */
    @TableField("session_key")
    private String sessionKey;
    /**
     * 市
     */
    @TableField("city")
    private String city;
    /**
     * 省
     */
    @TableField("province")
    private String province;
    /**
     * 国
     */
    @TableField("country")
    private String country;
    /**
     * 头像
     */
    @TableField("avatar_url")
    private String avatarUrl;
    /**
     * 性别
     */
    @TableField("gender")
    private Integer gender;
    /**
     * 网名
     */
    @TableField("nick_name")
    private String nickName;

    @TableField("accountName")
    private String accountName;

    @TableField("phoneNumber")
    private String phoneNumber;

    @TableField("identificationType")
    private String identificationType;

    @TableField("identificationNumber")
    private String identificationNumber;

    public String getAccountName() {
        return accountName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Date lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
