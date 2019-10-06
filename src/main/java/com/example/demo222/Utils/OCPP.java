package com.example.demo222.Utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

public class OCPP {
    public static String parseOCPPVin(String ocppPacket){
        JSONObject ocppJson = JSON.parseObject(ocppPacket);
        String vin = ocppJson.getString("data");
        return vin;
    }

    public static String parseOCPPCost(String ocppPacket){
        return "1";
    }

    public static Map<String, String> ocppAuthentication(String ocppPacket){
        JSONObject ocppJson = JSON.parseObject(ocppPacket);
        JSONObject properties = ocppJson.getJSONObject("properties");
        Map<String, String> parseResult = new HashMap<String, String>();
        parseResult.put("vin", properties.getString("data"));
        parseResult.put("vendorId", properties.getString("vendorId"));
        return parseResult;
    }

    public static Map<String, String> ocppPay(String ocppPacket){
        JSONObject ocppJson = JSON.parseObject(ocppPacket);
        JSONObject properties = ocppJson.getJSONObject("properties");
        Map<String, String> parseResult = new HashMap<String, String>();
        parseResult.put("vendorId", properties.getString("vendorId"));
        String meterStop = properties.getString("meterStop");
        String cost = String.valueOf(Float.parseFloat(meterStop) * 120);
        parseResult.put("cost", cost);
        return parseResult;
    }

    public static JSONObject ocppAuthenticationReturn(String result){
        JSONObject returnJson = new JSONObject();
        returnJson.put("title", "VinResponse");
        JSONObject properties = new JSONObject();
        properties.put("data", result);
        returnJson.put("properties", properties);
        return returnJson;
    }

    public static JSONObject ocppPayReturn(String result){
        JSONObject returnJson = new JSONObject();
        returnJson.put("title", "payResult");
        JSONObject properties2 = new JSONObject();
        properties2.put("status", result);
        JSONObject idTagInfo = new JSONObject();
        idTagInfo.put("properties", properties2);
        JSONObject properties1 = new JSONObject();
        properties1.put("idTagInfo", idTagInfo);
        returnJson.put("properties", properties1);
        return returnJson;
    }
}
