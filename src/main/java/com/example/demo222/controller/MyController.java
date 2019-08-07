package com.example.demo222.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo222.Utils.GUIDGenerator;
import com.example.demo222.entity.Card;
import com.example.demo222.entity.ReqUser;
import com.example.demo222.entity.Car;
import com.example.demo222.entity.User;
import com.example.demo222.repository.UserMapper;
import com.example.demo222.service.BindingService;
import com.example.demo222.service.HttpClient;
import com.example.demo222.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo222.Utils.XmlUtils.xml2Json;
import static com.example.demo222.service.BindingService.*;

/**
 * Created by 夏路遥
 * 8/1/2019 2:59 PM
 */

@RestController
@RequestMapping(value="/api/v1/user")
public class MyController {

    @Autowired
    HttpClient httpClient;
    @Autowired
    LoginService loginService;
    @Autowired
    BindingService bindingService;
    @Autowired
    private UserMapper userMapper;


    @PostMapping("/binding_1")
    public String bindingPost1(
            @RequestParam("TxCode") String txCode,
            @RequestParam("BankID") String bankID,
            @RequestParam("AccountName") String accountName,
            @RequestParam("AccountId") String accountId,
            @RequestParam("IdentificationType") String identificationType,
            @RequestParam("IdentificationNumber") String identificationNumber,
            @RequestParam("PhoneNumber") String phoneNumber,
            @RequestParam("CardType") String cardType,
            @RequestParam("cardNumber") String cardNumber
    ) throws Exception{
        System.out.println(txCode+","+bankID+","+accountName+","+accountId+","+identificationNumber);

        String url="http://localhost:8080/zhongjin_demo_war_exploded/Tx2531";
        HttpMethod method=HttpMethod.POST;
        String institutionID = "200027";
        String txSNBinding = GUIDGenerator.genGUID();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("InstitutionID",institutionID);
        params.add("TxCode",txCode);
        params.add("TxSNBinding",txSNBinding);
        params.add("BankID",bankID);
        params.add("AccountName",accountName);
        params.add("AccountNumber",cardNumber);
        params.add("IdentificationType",identificationType);
        params.add("IdentificationNumber",identificationNumber);
        params.add("PhoneNumber",phoneNumber);
        params.add("CardType",cardType);

//        //判断是否取到用户，如果没有就保存在数据库中
//        List<ReqUser> us = loginService.findByTxSnBinding(txSNBinding);
//
//        if(us.size()==0) {
//
//            ReqUser registers = new ReqUser();
//            registers.setInstitutionID(institutionID);
//            registers.setTxCode(txCode);
//            registers.setTxSnBinding(txSNBinding);
//            registers.setBankID(bankID);
//            registers.setAccountName(accountName);
//            registers.setAccountNumber(cardNumber);
//            registers.setIdentificationType(identificationType);
//            registers.setIdentificationNumber(identificationNumber);
//            registers.setPhoneNumber(phoneNumber);
//            registers.setCardType(cardType);
//
//            loginService.save(registers);
//        }
        List<Card> cards = bindingService.findByTxSNBinding(txSNBinding);
        if (cards.size()==0){
            Card card = new Card();
            card.setAccountId(accountId);
            card.setBankID(bankID);
            card.setCardNumber(cardNumber);
            card.setCardType(cardType);
            card.setTxSnBinding(txSNBinding);
            bindingService.saveCard(card);

            User user = new User();
            user.setAccountName(accountName);
            user.setIdentificationNumber(identificationNumber);
            user.setIdentificationType(identificationType);
            user.setPhoneNumber(phoneNumber);
            user.setAccountId(accountId);
            this.userMapper.updateById(user);
        }

//        String xmlStr= httpClient.client(url,method,params);

//        JSONObject json = xml2Json(httpClient.client(url,method,params));
//        return json.toJSONString();
        return "sucess";
    }

    @PostMapping("/binding_2")
    public String bindingPost2(
            @RequestParam("TxCode") String txCode,
            @RequestParam("sms_validation_code") int sms_validation_code,
            @RequestParam("AccountNumber") String accountNumber
    ) throws Exception {
        String url="http://localhost:8080/zhongjin_demo_war_exploded/Tx2532";
        HttpMethod method=HttpMethod.POST;
        String institutionID = "200027";
        String txSNBinding = loginService.findTxSNBindingByAccountNumber(accountNumber);
        MultiValueMap params = new LinkedMultiValueMap<>();
        params.add("InstitutionID",institutionID);
        params.add("TxCode",txCode);
        params.add("TxSNBinding",txSNBinding);
        params.add("SMSValidationCode", sms_validation_code);
//        String xmlStr= httpClient.client(url,method,params);
        JSONObject json = xml2Json(httpClient.client(url,method,params));
        return json.toJSONString();
    }

    @PostMapping("/bingding_car")
    public String binding_car(
            @RequestParam("AccountId") String accountId,
            @RequestParam("vin") String vin,
            @RequestParam("license_plate") String license_plate
    ) throws Exception {
        Car new_car = new Car();
        new_car.setAccountId(accountId);
        new_car.setVin(vin);
        new_car.setLicense_plate(license_plate);
        bindingService.saveCar(new_car);
        JSONObject json = new JSONObject();
        json.put("code", 200);
        json.put("status", true);
        return json.toJSONString();
    }

}



