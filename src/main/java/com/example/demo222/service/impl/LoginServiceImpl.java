package com.example.demo222.service.impl;

import com.example.demo222.entity.ReqUser;
import com.example.demo222.repository.MyRepository;
import com.example.demo222.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 夏路遥
 * 8/1/2019 10:12 AM
 */

@Service("loginService")
public class LoginServiceImpl implements LoginService {
    @Autowired
    MyRepository loginRepository;

    @Override
    public List<ReqUser> findAll() {
        return null;
    }
     //登陆
    @Override
    public List<ReqUser> findByAccountNameAndTxSnBinding(String accountName, String txSnBinding) {
        //return loginRepository.findAll();
        return loginRepository.findByAccountnameAndTxSnBinding(accountName,txSnBinding);
    }
     // 注册
    @Override
    public void save(ReqUser reqUser) {
        loginRepository.save(reqUser);
    }
     //查用户名
    @Override
    public List<ReqUser> findByTxSnBinding(String txSnBinding) {
        return loginRepository.findByTxSnBinding(txSnBinding);
    }

    //查找绑定流水号
    @Override
    public String findTxSNBindingByAccountNumber(String AccountNumber){
        return loginRepository.findTxSNBindingByAccountNumber(AccountNumber);
    }
}
