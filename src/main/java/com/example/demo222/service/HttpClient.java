package com.example.demo222.service;


import org.dom4j.DocumentException;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


/**
 * Created by 夏路遥
 * 7/31/2019 4:47 PM
 */
/** 
* @Description: 发送http post请求，返回得到的页面
* @Author: ZMD
* @UpdateTime: 2019/8/17 12:35
*/ 
@Service
public class HttpClient {

    public String client(String url, HttpMethod method, MultiValueMap<String, String> params) throws DocumentException {
        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response1 = template.postForEntity(url, params, String.class);

        String htmlStr = response1.getBody();
//        Document document = DocumentHelper.parseText(xmlStr);

        return htmlStr;
    }


}
