package com.example.demo222.Utils;

import com.alibaba.fastjson.JSONObject;
import org.jdom2.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @Description: 解析html 
* @Author: ZMD
* @UpdateTime: 2019/8/17 12:37
*/ 
public class htmlUtils {

    /** 
    * @Description: 从html中解析出xml 
    * @Author: ZMD
    * @UpdateTime: 2019/8/17 12:49
    */ 
    public static String getRequestXML(String html){
        Document document = Jsoup.parse(html);
        org.jsoup.select.Elements form = document.select("body");
        Element e = form.select("tbody").select("tr").next().select("td").next().select("textarea").first();
        String request = e.text();
        return request;
    }

    /** 
    * @Description: 从html中解析出xml 
    * @Author: ZMD
    * @UpdateTime: 2019/8/17 13:02
    */ 
    public static Map<String, String> getMessageAndSignature(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.select("body").select("form").select("input");
        Map<String, String> messageSignature = new HashMap<String, String>();
        messageSignature.put("message", elements.get(1).val());
        messageSignature.put("signature", elements.get(2).val());
        return messageSignature;
    }

    /** 
    * @Description: 从html中解析出请求返回结果 
    * @Author: ZMD
    * @UpdateTime: 2019/8/17 13:42
    */ 
    public static String getResponse(String html){
        System.out.println("response:\n"+html);
        Document document = Jsoup.parse(html);
        Elements elements = document.select("body").select("table").select("tbody").select("tr").next().select("td").next().select("textarea");
        JSONObject jsonObject = null;
        try {
            jsonObject = XmlUtils.xml2Json(elements.get(0).text());
            System.out.println("response:"+jsonObject);
        } catch (JDOMException e) {
            e.printStackTrace();
            return "get response error";
        } catch (IOException e) {
            e.printStackTrace();
            return "get response error";
        }
        JSONObject response = jsonObject.getJSONObject("Response");
        List head = response.getJSONArray("Head");
        JSONObject message = (JSONObject) head.get(0);
        return message.getString("Message");
    }
}
