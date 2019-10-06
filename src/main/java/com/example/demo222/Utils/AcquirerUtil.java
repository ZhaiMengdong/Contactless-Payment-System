package com.example.demo222.Utils;

import com.example.demo222.service.HttpClient;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

public class AcquirerUtil {

    HttpMethod method=HttpMethod.POST;
    String institutionID = "200027";//机构号
    String url2 = "http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/SendMessage";

    public String Tx2531(String txCode, String txSNBinding, String bankID, String accountName, String cardNumber, String identificationType, String identificationNumber, String phoneNumber, String cardType, HttpClient httpClient){

        String response = null;
        try {
            //绑卡（获取验证码）的URL，通过向该URL提交绑卡信息可以生成绑卡的xml、message和signature
            String url="http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2531";

            //封装绑卡（获取验证码）请求参数
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
            //ValidDate和CVN2为可选字段，如果不选，也应将其加入请求参数中，值为""
            params.add("ValidDate", "");
            params.add("CVN2", "");

            //向http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2531提交表单，得到返回页面
            String html = httpClient.client(url, method, params);
            //获取返回页面中的xml
            String requestXML = htmlUtils.getRequestXML(html);
            //从返回页面中获取message和signature
            Map<String, String> messageSignature = htmlUtils.getMessageAndSignature(html);

            //封装参数，进行最终的表单提交
            MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
            params2.add("RequestPlainText", requestXML);
            params2.add("message", messageSignature.get("message"));
            params2.add("signature", messageSignature.get("signature"));
            params2.add("txCode", "2531");
            params2.add("txName", "建立绑定关系（发送验证短信）");
            params2.add("Flag", "");
            String html2 = httpClient.client(url2, method, params2);
            //从返回的页面中解析绑卡（获取验证码）结果
            response = htmlUtils.getResponse(html2);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public String Tx2532(String txSNBinding, String sms_validation_code, HttpClient httpClient){
        String response = null;
        try {
            String url="http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2532";

            //封装绑定请求参数
            MultiValueMap params = new LinkedMultiValueMap<>();
            params.add("InstitutionID", institutionID);
            params.add("TxCode", "2532");
            params.add("TxSNBinding", txSNBinding);
            params.add("SMSValidationCode", sms_validation_code);
            params.add("ValidDate", "");
            params.add("CVN2", "");

            String html = httpClient.client(url, method, params);
            String requestXML = htmlUtils.getRequestXML(html);
            Map<String, String> messageSignature = htmlUtils.getMessageAndSignature(html);

            MultiValueMap<String,String> params2 = new LinkedMultiValueMap<>();
            params2.add("RequestPlainText", requestXML);
            params2.add("message", messageSignature.get("message"));
            params2.add("signature", messageSignature.get("signature"));
            params2.add("txCode", "2532");
            params2.add("txName", "建立绑定关系（验证和绑定）");
            params2.add("Flag", "");
            String html2 = httpClient.client(url2, method, params2);
            response = htmlUtils.getResponse(html2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    public String Tx2511(String PaymentNo, String cost, String TxSNBinding, HttpClient httpClient){
        String response = null;
        try{
            String url="http://localhost:8080/zhongjin-demo-1.0-SNAPSHOT/Tx2511";
            MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
            params.add("InstitutionID",institutionID);
            params.add("PaymentNo", PaymentNo);
            params.add("TxCode","2511");
            params.add("Amount", "1");
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
            response = htmlUtils.getResponse(html2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
