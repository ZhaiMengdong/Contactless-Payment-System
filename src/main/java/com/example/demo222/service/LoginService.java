package com.example.demo222.service;

import com.example.demo222.entity.ReqUser;

import java.util.List;

/**
 * Created by 夏路遥
 * 8/1/2019 10:42 AM
 */

public interface LoginService {
     List<ReqUser> findAll();

     //登陆
     List<ReqUser> findByAccountNameAndTxSnBinding(String accountName, String txSnBinding);

     //注册
     void save(ReqUser reqUser);

     List<ReqUser> findByTxSnBinding(String txSnBinding);

     String findTxSNBindingByAccountNumber(String AccountNumber);
}
