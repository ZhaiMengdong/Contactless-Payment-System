package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("Binding")
public class Binding {

    @TableField("accountId")
    private String accountId;

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
