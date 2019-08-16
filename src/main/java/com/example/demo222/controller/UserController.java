package com.example.demo222.controller;

import com.example.demo222.Utils.GlobalResult;
import com.example.demo222.Utils.WechatUtil;
import com.example.demo222.entity.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo222.repository.UserMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Date;

/**
 * Created by 夏路遥
 * 8/1/2019 2:54 PM
 */

@Controller
public class UserController {

    @Autowired
    private UserMapper userMapper;


    /*
    小程序登录，给小程序返回openId，小程序可以通过openId来标记自身信息
     */
    @PostMapping("wx/login")
    @ResponseBody
    public GlobalResult user_login(
            @RequestParam("code") String code
    ){
        System.out.println("accept code:"+code);
        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);
        String openid = SessionKeyOpenId.getString("openid");
        String sessionKey = SessionKeyOpenId.getString("session_key");
        User user = this.userMapper.selectById(openid);
        if (user == null){
            user = new User();
            user.setAccountId(openid);
            user.setSessionKey(sessionKey);
            user.setLastVisitTime(new Date());
            this.userMapper.insert(user);
            Map<String, String> result = new HashMap<>();
//        result.put("session_key", sessionKey);
            result.put("AccountId", openid);
            return GlobalResult.build(200, null, result);
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("AccountId", user.getAccountId());
            return GlobalResult.build(200, null, result);
        }

    }

    /**
     * 微信用户注册
     */
    @PostMapping("wx/regesiter")
    @ResponseBody
    public GlobalResult user_regesiger(@RequestParam(value = "AccountId", required = false) String accountId,
                                   @RequestParam(value = "rawData", required = false) String rawData,
                                   @RequestParam(value = "signature", required = false) String signature,
                                   @RequestParam(value = "encrypteData", required = false) String encrypteData,
                                   @RequestParam(value = "iv", required = false) String iv) {
        // 用户非敏感信息：rawData
        // 签名：signature
        JSONObject rawDataJson = JSON.parseObject(rawData);
        // 1.接收小程序发送的code

        // 2.开发者服务器 登录凭证校验接口 appi + appsecret + code
//        JSONObject SessionKeyOpenId = WechatUtil.getSessionKeyOrOpenId(code);

        // 3.接收微信接口服务 获取返回的参数
//        String openid = SessionKeyOpenId.getString("openid");
//        String sessionKey = SessionKeyOpenId.getString("session_key");
        User user = this.userMapper.selectById(accountId);
        String sessionKey = user.getSessionKey();

        // 4.校验签名 小程序发送的签名signature与服务器端生成的签名signature2 = sha1(rawData + sessionKey)
        String signature2 = DigestUtils.sha1Hex(rawData + sessionKey);
        if (!signature.equals(signature2)) {
            return GlobalResult.build(500, "签名校验失败", null);
        }

        String nickName = rawDataJson.getString("nickName");
        String avatarUrl = rawDataJson.getString("avatarUrl");
        String gender = rawDataJson.getString("gender");
        String city = rawDataJson.getString("city");
        String country = rawDataJson.getString("country");
        String province = rawDataJson.getString("province");

        user = new User();
        user.setCreateTime(new Date());
        user.setLastVisitTime(new Date());
        user.setCity(city);
        user.setProvince(province);
        user.setCountry(country);
        user.setAvatarUrl(avatarUrl);
        user.setGender(Integer.parseInt(gender));
        user.setNickName(nickName);

        this.userMapper.insert(user);
        // 5.根据返回的User实体类，判断用户是否是新用户，是的话，将用户信息存到数据库；不是的话，更新最新登录时间


        // uuid生成唯一key，用于维护微信小程序用户与服务端的会话
//        String skey = UUID.randomUUID().toString();
//        if (user == null) {
//            // 用户信息入库
//
//        } else {
//            // 已存在，更新用户登录时间
//            user.setLastVisitTime(new Date());
//            // 重新设置会话skey
//            user.setSkey(skey);
//            this.userMapper.updateById(user);
//        }
        Map<String, String> result = new HashMap<>();
        result.put("status", "OK");
        return GlobalResult.build(200, null, result);
    }



}
