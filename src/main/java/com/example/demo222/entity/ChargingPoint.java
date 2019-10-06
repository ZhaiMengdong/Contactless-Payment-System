package com.example.demo222.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("ChargingPoint")
public class ChargingPoint {
    @TableField("id")
    private String id;

    @TableField("position")
    private String position;

    public String getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
