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
 * @Author: zmd
 * @Date: 2019/8/15 17:15
 **/
public class htmlUtils {

    public static String getRequestXML(String html){
//        System.out.println("html"+html);
        Document document = Jsoup.parse(html);
        org.jsoup.select.Elements form = document.select("body");
        Element e = form.select("tbody").select("tr").next().select("td").next().select("textarea").first();
        String request = e.text();
//        System.out.println("xml:"+request);
        return request;
    }

    public static Map<String, String> getMessageAndSignature(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.select("body").select("form").select("input");
        Map<String, String> messageSignature = new HashMap<String, String>();
        messageSignature.put("message", elements.get(1).val());
        messageSignature.put("signature", elements.get(2).val());
        return messageSignature;
    }

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
