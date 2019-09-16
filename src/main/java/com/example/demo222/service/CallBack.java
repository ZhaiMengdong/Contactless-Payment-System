package com.example.demo222.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo222.Utils.GUIDGenerator;
import com.example.demo222.Utils.OCPP;
import com.example.demo222.Utils.SM4Util;
import com.example.demo222.Utils.htmlUtils;
import com.example.demo222.entity.Binding;
import com.example.demo222.entity.Car;
import com.example.demo222.entity.Card;
import com.example.demo222.entity.User;
import com.example.demo222.repository.CarMapper;
import com.example.demo222.repository.CardMapper;
import org.apache.commons.lang.StringUtils;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.apache.commons.lang3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static javax.xml.transform.OutputKeys.ENCODING;


public class CallBack implements MqttCallback {


    public static String broker = "tcp://127.0.0.1:1883";
    public static MemoryPersistence persistence = new MemoryPersistence();
    public static String clientId = "cloud_client_return";
    public static final byte[] key = new byte[]{-30, -81, -101, 119, -50, -80, -63, 111, -49, 33, -5, 94, -75, 88, -73, -45};
    public HttpClient httpClient = new HttpClient();

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，可以做重连");
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后得到的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));

        byte [] data = new byte[1024];
        byte [] payloadBytesDecrypt = null;
        System.out.println("key: "+Arrays.toString(key));
        byte [] payloadBytesEncrypt = message.getPayload();
        System.out.println("payloadBytes: "+Arrays.toString(payloadBytesEncrypt));
        payloadBytesDecrypt = SM4Util.decrypt_Ecb_NoPadding(key, payloadBytesEncrypt);
        System.out.println("解密后字符：" + Arrays.toString(payloadBytesDecrypt));

        String payload = new String(payloadBytesDecrypt);
        System.out.println("解密后字符串："+ payload);

        int endFlagIndex = payload.indexOf("E_N_D");
        JSONObject jsonObj2 = JSON.parseObject(payload.substring(0,endFlagIndex));
        String ocppPacket = jsonObj2.get("data_recv").toString();
        String clientAddr = jsonObj2.get("client_addr").toString();
        String gatewayId = jsonObj2.getString("gateway_id");
        System.out.println("ocppPacket: "+ocppPacket);

        JSONObject jsonObj3 = new JSONObject();
        jsonObj3.put("client_addr", clientAddr);

        Connection conn = null;
        Statement stmt = null;
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","root","123456");
        stmt = conn.createStatement();

        if(topic.equals("authentication")){
            String vin = OCPP.parseOCPPVin(ocppPacket);

            String sql = "select * from car where vin=\'"+vin+"\'";
            ResultSet cars = stmt.executeQuery(sql);
            if (cars.next()){
                jsonObj3.put("result", "ok");
            }else {
                jsonObj3.put("result", "error");
            }
        }else if (topic.equals("payment")){
            String cost = OCPP.parseOCPPCost(ocppPacket);
            String vin = OCPP.parseOCPPVin(ocppPacket);

            String sql = "select accountId from car where vin=\'"+vin+"\'";
            ResultSet car = stmt.executeQuery(sql);
            car.next();
            String accountId = car.getString("accountId");

            sql = "select * from card where accountId=\'"+accountId+"\' order by priority DESC";
            ResultSet cards = stmt.executeQuery(sql);
            cards.next();
            String TxSNBinding = cards.getString("TxSNBinding");

            String url="http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2511";
            HttpMethod method = HttpMethod.POST;
            String institutionID = "200027";//机构号
            String PaymentNo = GUIDGenerator.genGUID();
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("InstitutionID",institutionID);
            params.add("PaymentNo", PaymentNo);
            params.add("TxCode","2511");
            params.add("Amount", cost);
            params.add("TxSNBinding", TxSNBinding);
            params.add("SplitType", "10");
            params.add("SettlementFlag","0001");
            //ValidDate和CVN2为可选字段，如果不选，也应将其加入请求参数中，值为""
            params.add("ValidDate", "");
            params.add("CVN2", "");
            params.add("SharedInstitutionID", "");
            params.add("Remark","");

            //向http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2511提交表单，得到返回页面
            String html = httpClient.client(url, method, params);
            System.out.println(html);
            //获取返回页面中的xml
            String requestXML = htmlUtils.getRequestXML(html);
            //从返回页面中获取message和signature
            Map<String, String> messageSignature = htmlUtils.getMessageAndSignature(html);
            MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
            params2.add("RequestPlainText", requestXML);
            params2.add("message", messageSignature.get("message"));
            params2.add("signature", messageSignature.get("signature"));
            params2.add("txCode", "2511");
            params2.add("txName", "快捷支付");
            params2.add("Flag", "");
            String url2 = "http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/SendMessage";
            String html2 = httpClient.client(url2, method, params2);
            String response = htmlUtils.getResponse(html2);
            System.out.println("response: "+response);
            jsonObj3.put("result", "ok");
        }

        System.out.println(jsonObj3.toString());
        System.arraycopy(jsonObj3.toString().getBytes(),0,data,0,jsonObj3.toString().length());
        byte [] returnDataBytes = SM4Util.encrypt_Ecb_NoPadding(key, data);
        System.out.println(Arrays.toString(returnDataBytes));
        mqtt_publish(topic, returnDataBytes, gatewayId);
    }

    public void mqtt_publish(String topic, byte [] message, String gatewayId){
        String resultTopic = null;
        if (topic.equals("authentication")){
            resultTopic = gatewayId + "_authentication";
        }else if (topic.equals("payment")){
            resultTopic = gatewayId + "_payment";
        }
        System.out.println("resultTopic: "+resultTopic);
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            mqttClient.connect(options);
//            MqttMessage result = new MqttMessage(message.getBytes());
            MqttMessage result = new MqttMessage(message);
            result.setQos(2);
            mqttClient.publish(resultTopic, result);
            System.out.println("已回复payload");
            mqttClient.disconnect();
            System.out.println("已断开本次连接");
            mqttClient.close();
        } catch (MqttPersistenceException e) {
            e.printStackTrace();
        } catch (MqttSecurityException e) {
            e.printStackTrace();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public static String toHexString4(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        // 使用String的format方法进行转换
        for (byte b : bytes) {
            sb.append(String.format("%02x", new Integer(b & 0xff)));
        }
        return sb.toString();
    }

}