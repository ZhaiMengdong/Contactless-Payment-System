package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/** 
* @Description:
 * 表binding，在获取短信验证码时记录accoutid和绑定流水号的对应关系，
 * 方便在提交短信验证码时获取验证码对应的绑定流水号
* @Author: ZMD
* @UpdateTime: 2019/8/17 11:21
*/ 
@Data
@TableName("Binding")
public class Binding {

    @TableField("accountId")
    private String accountId;

    //绑定流水号
    @TableField("TxSnBinding")
    private String TxSnBinding;

    public String getAccountId() {
        return accountId;
    }

    public String getTxSnBinding() {
        return TxSnBinding;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setTxSnBinding(String txSnBinding) {
        TxSnBinding = txSnBinding;
    }
}
