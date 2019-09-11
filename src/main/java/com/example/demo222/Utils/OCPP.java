package com.example.demo222.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class OCPP {
    public static String parseOCPPVin(String ocppPacket){
        JSONObject ocppJson = JSON.parseObject(ocppPacket);
        String vin = ocppJson.getString("data");
        return vin;
    }

    public static String parseOCPPCost(String ocppPacket){
        return "1";
    }
}
