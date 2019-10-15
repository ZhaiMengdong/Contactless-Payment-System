package com.example.demo222.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo222.Utils.*;
import com.example.demo222.entity.Bill;
import com.example.demo222.entity.Car;
import com.example.demo222.entity.Card;
import com.example.demo222.repository.BillMapper;
import com.example.demo222.repository.CarMapper;
import com.example.demo222.repository.CardMapper;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CallBack implements MqttCallback {

    private CarMapper carMapper ;
    private CardMapper cardMapper;
    private BillMapper billMapper;
    private HttpClient httpClient;

    public static String broker = "tcp://127.0.0.1:1883";
    public static MemoryPersistence persistence = new MemoryPersistence();
    public static String clientId = "cloud_client_return";
    public static final byte[] key = new byte[]{-30, -81, -101, 119, -50, -80, -63, 111, -49, 33, -5, 94, -75, 88, -73, -45};

    public CallBack(CarMapper carMapper, CardMapper cardMapper, HttpClient httpClient, BillMapper billMapper){
        this.carMapper = carMapper;
        this.cardMapper = cardMapper;
        this.httpClient = httpClient;
        this.billMapper = billMapper;
    }

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
        System.out.println("key: "+ Arrays.toString(key));
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

        if(topic.equals("authentication")){
//            String vin = OCPP.parseOCPPVin(ocppPacket);
            Map<String, String> ocppInfo = OCPP.ocppAuthentication(ocppPacket);
            String vin = ocppInfo.get("vin");
            String vendorId = ocppInfo.get("vendorId");
            QueryWrapper<Car> wrapper = new QueryWrapper<>();
            wrapper.eq("Vin", vin);
            List<Car> cars = new ArrayList<Car>();
            cars = this.carMapper.selectList(wrapper);
            if(cars.size() == 0){
                jsonObj3.put("result", OCPP.ocppAuthenticationReturn("0").toString());
            }else {
                jsonObj3.put("result", OCPP.ocppAuthenticationReturn("1").toString());
                Bill bill = new Bill();
                bill.setDeviceNumber(vendorId);
                bill.setVin(vin);
                bill.setStatus("0");
                bill.setStartTime(new Date());
                billMapper.insert(bill);
            }
        }else if (topic.equals("payment")){
            Map<String, String> ocppInfo = OCPP.ocppPay(ocppPacket);
            String cost = ocppInfo.get("cost");
            String vendorId = ocppInfo.get("vendorId");
            QueryWrapper<Bill> wrapper0 = new QueryWrapper<>();
            wrapper0.eq("deviceNumber", vendorId);
            wrapper0.eq("status", "0");
            Bill billResult = billMapper.selectOne(wrapper0);
            String vin = billResult.getVin();

            QueryWrapper<Car> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("Vin", vin);
            Car carResult = this.carMapper.selectOne(wrapper1);
            String accountId = carResult.getAccountId();
            QueryWrapper<Card> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("accountId", accountId);
            wrapper2.orderByAsc("priority");
            List<Card> cards = new ArrayList<>();
            cards = cardMapper.selectList(wrapper2);
            String TxSNBinding = cards.get(0).getTxSnBinding();
            String PaymentNo = GUIDGenerator.genGUID();

            AcquirerUtil acquirerUtil = new AcquirerUtil();
            String response = acquirerUtil.Tx2511(PaymentNo, cost, TxSNBinding, httpClient);
            System.out.println("response: "+response);
            if(response.equals("OK.")){
                jsonObj3.put("result", OCPP.ocppPayReturn("1").toString());
                String cardNumber = cards.get(0).getCardNumber();
                QueryWrapper<Bill> queryWrapper = new QueryWrapper();
                queryWrapper.eq("status", "0");
                queryWrapper.eq("deviceNumber", vendorId);
                Bill bill = new Bill();
                bill.setAccountId(accountId);
                bill.setAmount(cost);
                bill.setCardNumber(cardNumber);
                bill.setInstitutionId("200027");
                bill.setPaymentNumber(PaymentNo);
                bill.setStatus("1");
                bill.setEndTime(new Date());
                billMapper.update(bill, queryWrapper);
            }else {
                QueryWrapper<Bill> queryWrapper = new QueryWrapper();
                queryWrapper.eq("status", "0");
                queryWrapper.eq("deviceNumber", vendorId);
                Bill bill = new Bill();
                bill.setStatus("-1");
                billMapper.update(bill, queryWrapper);
                jsonObj3.put("result", OCPP.ocppPayReturn("0").toString());
            }
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
        try {
            MqttClient mqttClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            mqttClient.connect(options);
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
