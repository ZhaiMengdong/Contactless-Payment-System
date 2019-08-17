package com.example.demo222.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo222.Utils.GUIDGenerator;
import com.example.demo222.Utils.GlobalResult;
import com.example.demo222.entity.Bill;
import com.example.demo222.entity.Car;
import com.example.demo222.entity.Card;
import com.example.demo222.repository.BillMapper;
import com.example.demo222.repository.CarMapper;
import com.example.demo222.repository.CardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** 
* @Description: 对接边缘计算网关，无感支付车辆认证以及无感支付订单提交接口 
* @Author: ZMD
* @UpdateTime: 2019/8/17 11:07
*/ 
@Controller
@RequestMapping("/api/v1/trade")
public class Trade {

    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private BillMapper billMapper;

    /** 
    * @Description: 充电前判断车辆是否已开通无感支付 
    * @Author: ZMD
    * @UpdateTime: 2019/8/17 11:16
    */ 
    @RequestMapping("/charging_verification")
    public GlobalResult chargingVerification(
            @RequestParam("vin") String vin,
            @RequestParam("license_plate") String license_plate
    ) throws Exception {
        QueryWrapper<Car> wrapperCar = new QueryWrapper<Car>();
        if (vin != null){
            wrapperCar.eq("vin", vin);
        } else {
            wrapperCar.eq("license_plate", license_plate);
        }
        Car car = this.carMapper.selectOne(wrapperCar);
        String accountId = car.getAccountId();
        QueryWrapper<Card> wrapperCard = new QueryWrapper<Card>();
        wrapperCard.eq("accountId", accountId);
        List<Card> cards = this.cardMapper.selectList(wrapperCard);
        Map<String, String> result = new HashMap<String, String>();
        if (cards.size() == 0){
            result.put("status", "failed");
        } else {
            result.put("status", "success");
        }
        return GlobalResult.build(200,null, result);
    }

    /** 
    * @Description: 无感充电支付订单提交 
    * @Author: ZMD
    * @UpdateTime: 2019/8/17 11:34
    */ 
    @RequestMapping("/charging_pay")
    public GlobalResult chargingPay(
            @RequestParam("amount") String amount,
            @RequestParam("vin") String vin,
            @RequestParam("license_plate") String license_plate,
            @RequestParam("deviceNumber") String deviceNumber
    )throws Exception{
        QueryWrapper<Car> wrapperCar = new QueryWrapper<Car>();
        if (vin != null){
            wrapperCar.eq("vin", vin);
        } else {
            wrapperCar.eq("license_plate", license_plate);
        }
        Car car = this.carMapper.selectOne(wrapperCar);
        String accountId = car.getAccountId();
        QueryWrapper<Card> wrapperCard = new QueryWrapper<Card>();
        wrapperCard.eq("accountId", accountId);
        wrapperCar.orderByAsc("priority");
        List<Card> cards = this.cardMapper.selectList(wrapperCard);
        String paymentNo = GUIDGenerator.genGUID();
        String status;
        if (cards.size() != 0){
            String url="http://localhost:8080/zhongjin_demo_war_exploded/Tx2511";
            HttpMethod method=HttpMethod.POST;
            String institutionID = "200027";
            MultiValueMap params = new LinkedMultiValueMap<>();
            params.add("InstitutionID",institutionID);
            params.add("Amount", amount);
            params.add("PaymentNo", paymentNo);
            params.add("TxSNBinding", cards.get(0).getTxSnBinding());
            params.add("SettlementFlag", "10");

            String cardNumber = cards.get(0).getCardNumber();
            Bill bill = new Bill();
            bill.setAccountId(accountId);
            bill.setAmount(amount);
            bill.setCardNumber(cardNumber);
            bill.setTime(new Date());
            bill.setInstitutionId(institutionID);
            bill.setStatus("交易中");
            bill.setPaymentNumber(paymentNo);
            bill.setDeviceNumber(deviceNumber);
            this.billMapper.insert(bill);


//            HttpClient httpClient = new HttpClient();
//            JSONObject json = xml2Json(httpClient.client(url,method,params));

            status = "交易成功";
            updateBillStatus(paymentNo, status);

            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "OK");
            return GlobalResult.build(200, null, result);
        }

        status = "交易失败";
        updateBillStatus(paymentNo, status);

        Map<String, String> result = new HashMap<>();
//        result.put("session_key", sessionKey);
        result.put("Confirm", "failed");
        return GlobalResult.build(200, null, result);
    }


    public void updateBillStatus(String paymentNo, String status){
        QueryWrapper<Bill> wrapper = new QueryWrapper<Bill>();
        wrapper.eq("paymentNumber", paymentNo);
        Bill bill1 = new Bill();
        bill1.setStatus(status);
        this.billMapper.update(bill1, wrapper);
    }

}
