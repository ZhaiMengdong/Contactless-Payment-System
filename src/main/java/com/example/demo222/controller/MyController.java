package com.example.demo222.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo222.Utils.AcquirerUtil;
import com.example.demo222.Utils.GUIDGenerator;
import com.example.demo222.Utils.GlobalResult;
import com.example.demo222.Utils.htmlUtils;
import com.example.demo222.entity.*;
import com.example.demo222.repository.*;
import com.example.demo222.service.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value="/api/v1/user")
public class MyController {

    @Autowired
    HttpClient httpClient;
//    @Autowired
//    BindingService bindingService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CarMapper carMapper;
    @Autowired
    private CardMapper cardMapper;
    @Autowired
    private BindingInfo bindingInfo;
    @Autowired
    private BillMapper billMapper;
    @Autowired
    private ChargingPointMapper chargingPointMapper;
    private AcquirerUtil acquirerUtil;

    /** 
    * @Description: 第一次绑定请求，提交银行卡绑定相关信息以获取短信验证码 
    * @Author: ZMD
    * @Date: 2019/8/16 17:58
    */ 
    @PostMapping("/binding_1")
    public GlobalResult bindingPost1(
            @RequestParam("TxCode") String txCode,//交易编码
            @RequestParam("Bank") String bank,//银行名字
            @RequestParam("AccountName") String accountName,//持卡人名字
            @RequestParam("AccountId") String accountId,//账号
            @RequestParam("IdentificationType") String idType,//证件类型
            @RequestParam("IdentificationNumber") String identificationNumber,//证件号
            @RequestParam("PhoneNumber") String phoneNumber,//预留手机号
            @RequestParam("CardType") String cardTypeCn,//银行卡类型
            @RequestParam("CardNumber") String cardNumber//银行卡卡号
    ) throws Exception{

        //银行名字转代号
        String bankID = "000";
        switch (bank){
            case "中国工商银行":
                bankID = "102";
                break;
            case "中国农业银行":
                bankID = "103";
                break;
            case "中国银行":
                bankID = "104";
                break;
            case "中国建设银行":
                bankID = "105";
                break;
            case "招商银行":
                bankID = "308";
                break;
        }

        //证件类型转代号
        String identificationType = "-1";
        switch (idType){
            case "身份证":
                identificationType = "0";
                break;
            case "户口簿":
                identificationType = "1";
                break;
            case "护照":
                identificationType = "2";
                break;
        }

        //银行卡类型转代号
        String cardType = "00";
        switch (cardTypeCn){
            case "个人借记":
                cardType = "10";
                break;
            case "个人贷记":
                cardType = "20";
                break;
        }

        //生成绑定流水号
        String txSNBinding = GUIDGenerator.genGUID();
        //从返回的页面中解析绑卡（获取验证码）结果
        String response = acquirerUtil.Tx2531(txCode, txSNBinding, bankID, accountName, cardNumber, identificationType, identificationNumber, phoneNumber, cardType, httpClient);

        //如果获取验证码成功，则将本次绑卡信息写入表binding、card和user
        if (response.equals("OK")){

            QueryWrapper<Binding> wrapper = new QueryWrapper<Binding>();
            wrapper.eq("AccountId", accountId);
            Binding bindingResult = this.bindingInfo.selectOne(wrapper);
            //如果表中没有该用户的绑定记录，则写入一条新记录；如果已有一条记录，则删除旧记录，再写入新记录
            if (bindingResult == null) {
                Binding binding = new Binding();
                binding.setTxSnBinding(txSNBinding);
                binding.setAccountId(accountId);
                this.bindingInfo.insert(binding);
            } else {
                this.bindingInfo.delete(wrapper);
                Binding binding = new Binding();
                binding.setTxSnBinding(txSNBinding);
                binding.setAccountId(accountId);
                this.bindingInfo.insert(binding);
            }

            //如果数据库中没有本次绑定的银行卡，则添加一条银行卡记录，并更新用户信息；如果已绑定过该卡，则不作操作
            QueryWrapper<Card> wrapper1 = new QueryWrapper<Card>();
            wrapper1.eq("cardNumber", cardNumber);
            wrapper1.eq("AccountId", accountId);
            List<Card> cards = cardMapper.selectList(wrapper1);
            if (cards.size() == 0){
                Card card = new Card();
                card.setAccountId(accountId);
                card.setBankID(bankID);
                card.setCardNumber(cardNumber);
                card.setCardType(cardType);
                card.setTxSnBinding(txSNBinding);
                int priority = getLowestPriority(accountId) + 1;
                String tailNumber = cardNumber.substring(cardNumber.length()-4, cardNumber.length());
                card.setTailNumber(tailNumber);
                card.setPriority(priority);
                card.setStatus(0);
                this.cardMapper.insert(card);

                User user = new User();
                user.setAccountName(accountName);
                user.setIdentificationNumber(identificationNumber);
                user.setIdentificationType(identificationType);
                user.setPhoneNumber(phoneNumber);
                user.setAccountId(accountId);
                this.userMapper.updateById(user);
            }
            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "OK");
            return GlobalResult.build(200, null, result);
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "binding failed");
            return GlobalResult.build(200, null, result);
        }

    }

    /** 
    * @Description: 提交短信验证码，完成银行卡绑定 
    * @Author: ZMD
    * @Date: 2019/8/16 17:59
    */ 
    @PostMapping("/binding_2")
    public GlobalResult bindingPost2(
            @RequestParam("TxCode") String txCode,//交易编码
            @RequestParam("Sms_validation_code") String sms_validation_code,//短信验证码
            @RequestParam("AccountId") String accountId//账号
    ) throws Exception {

        //如果表binding中没有该账号的绑定记录，则要求先绑卡
        QueryWrapper<Binding> wrapper = new QueryWrapper<Binding>();
        wrapper.eq("AccountId", accountId);
        Binding bindingResult = this.bindingInfo.selectOne(wrapper);
        if (bindingResult == null){
            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "Please submit your card information first.");
            return GlobalResult.build(200, null, result);
        }
        //从数据库中得到绑定流水号
        String txSNBinding = bindingResult.getTxSnBinding();

        String response = acquirerUtil.Tx2532(txSNBinding, sms_validation_code, httpClient);

        //如果绑卡成功，则删除表binding中的绑卡记录
        if (response.equals("OK.")){
            wrapper.eq("AccountId", accountId);
            this.bindingInfo.delete(wrapper);

            //更新银行卡状态
            QueryWrapper<Card> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("AccountId", accountId);
            wrapper1.eq("txSnBinding", txSNBinding);
            Card card = new Card();
            card.setStatus(1);
            this.cardMapper.update(card, wrapper1);

            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "OK");
            return GlobalResult.build(200, null, result);
        } else {
            Map<String, String> result = new HashMap<>();
            result.put("Confirm", "sms validation code error");
            return GlobalResult.build(200, null, result);
        }
    }

    /** 
    * @Description: 为车辆开通无感支付 
    * @Author: ZMD
    * @Date: 2019/8/16 17:59
    */ 
    @PostMapping("/binding_car")
    public GlobalResult binding_car(
            @RequestParam("AccountId") String accountId,//账号
            @RequestParam("Vin") String vin,//车架号
            @RequestParam("License_plate") String licensePlate//车牌号
    ) throws Exception {
        Car new_car = new Car();
        new_car.setAccountId(accountId);
        new_car.setVin(vin);
        new_car.setLicensePlate(licensePlate);
        this.carMapper.insert(new_car);

        Map<String, String> result = new HashMap<String, String>();
        result.put("Confirm", "OK");
        return GlobalResult.build(200, null, result);
    }

    /** 
    * @Description: 为小程序提供该账号绑定银行卡的查询功能 
    * @Author: ZMD
    * @Date: 2019/8/16 18:00
    */ 
    @PostMapping("/get_user_cards")
    public GlobalResult get_user_cards(
            @RequestParam("AccountId") String accountId
    ) throws Exception{
        //查询该账号绑定的银行卡信息，并根据优先级排序
        QueryWrapper<Card> queryWrapper = new QueryWrapper<Card>();
        queryWrapper.eq("accountId", accountId);
        queryWrapper.orderByAsc("priority");
        List<Card> cards = this.cardMapper.selectList(queryWrapper);

        List<Map> result = new ArrayList<Map>();
        for (int i=0;i<cards.size();i++){
            Map<String, String> cardInfo = new HashMap<String, String>();

            //银行代号转银行名字
            String bank = "未知银行";
            switch (cards.get(i).getBankID()){
                case "102":
                    bank = "中国工商银行";
                    break;
                case "103":
                    bank = "中国农业银行";
                    break;
                case "104":
                    bank = "中国银行";
                    break;
                case "105":
                    bank = "中国建设银行";
                    break;
                case "308":
                    bank = "招商银行";
                    break;
            }

            //银行卡类型代号转银行卡类型
            String CardType = "未知卡种";
            switch (cards.get(i).getCardType()){
                case "10":
                    CardType = "个人借记";
                    break;
                case "20":
                    CardType = "个人贷记";
                    break;
            }
            cardInfo.put("Bank", bank);
            cardInfo.put("CardType", CardType);
            cardInfo.put("CardNumber", cards.get(i).getCardNumber());
            cardInfo.put("tailNumber", cards.get(i).getTailNumber());
            result.add(cardInfo);
        }
        return GlobalResult.build(200, null, result);
    }

    /** 
    * @Description: 为小程序提供开通无感支付车辆信息的查询功能
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:48
    */ 
    @PostMapping("/get_user_cars")
    public GlobalResult get_user_cars(
            @RequestParam("AccountId") String accountId
    )throws Exception{
        QueryWrapper<Car> queryWrapper = new QueryWrapper<Car>();
        queryWrapper.eq("accountId", accountId);
        List<Car> cars = this.carMapper.selectList(queryWrapper);
        List<Map> result = new ArrayList<Map>();
        for (int i=0;i<cars.size();i++){
            Map<String, String> carInfo = new HashMap<String,String>();
            System.out.println(cars.get(i).getLicensePlate());
            carInfo.put("License_plate", cars.get(i).getLicensePlate());
            carInfo.put("Vin", cars.get(i).getVin());
            result.add(carInfo);
        }
        return GlobalResult.build(200, null, result);
    }

    /**
    * @Description: 删除用户绑定的银行卡
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:57
    */
    @RequestMapping("/delete_user_card")
    public GlobalResult delete_user_card(
            @RequestParam("AccountId") String accountId,
            @RequestParam("AccountNumber") String cardNumber
    )throws Exception{
        QueryWrapper<Card> wrapper = new QueryWrapper<Card>();
        wrapper.eq("accountId", accountId);
        wrapper.eq("cardNumber", cardNumber);
        this.cardMapper.delete(wrapper);
        Map<String, String> result = new HashMap<String, String>();
        result.put("Confirm", "OK");
        return GlobalResult.build(200, null, result);
    }

    /**
    * @Description: 删除开通无感支付的车辆
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:57
    */
    @RequestMapping("/delete_user_car")
    public GlobalResult delete_user_car(
            @RequestParam("AccountId") String accountId,
            @RequestParam("Vin") String vin
    )throws Exception{
        QueryWrapper<Car> wrapper = new QueryWrapper<Car>();
        wrapper.eq("accountId", accountId);
        wrapper.eq("vin", vin);
        this.carMapper.delete(wrapper);
        Map<String, String> result = new HashMap<String, String>();
        result.put("Confirm", "OK");
        return GlobalResult.build(200, null, result);
    }

    /**
    * @Description: 获取账单信息
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:57
    */
    @RequestMapping("/get_user_bill")
    public GlobalResult getUserBill(
            @RequestParam("AccountId") String accountId
    )throws Exception{
        QueryWrapper<Bill> wrapper = new QueryWrapper<Bill>();
        wrapper.eq("accountId", accountId);
        List<Bill> bills = new ArrayList<Bill>();
        bills = this.billMapper.selectList(wrapper);
        List<Map> result = new ArrayList<Map>();
        for (int i=0;i<bills.size();i++){
            Map<String, String> bill = new HashMap<String, String>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            bill.put("billTime", sdf.format(bills.get(i).getEndTime()));
            bill.put("amount", bills.get(i).getAmount());
            bill.put("cardNumber", bills.get(i).getCardNumber().substring(bills.get(i).getCardNumber().length()-4, bills.get(i).getCardNumber().length()));
            bill.put("paymentNumber", bills.get(i).getPaymentNumber());
            bill.put("startTime", sdf.format(bills.get(i).getStartTime()));
            bill.put("endTime", sdf.format(bills.get(i).getEndTime()));
            bill.put("duration", String.valueOf(getDistanceTime(bills.get(i).getStartTime(), bills.get(i).getEndTime())));
            String deviceNumber = bills.get(i).getDeviceNumber();

            QueryWrapper<ChargingPoint> chargingPointQueryWrapper = new QueryWrapper<>();
            chargingPointQueryWrapper.eq("id", deviceNumber);
            ChargingPoint chargingPoint = this.chargingPointMapper.selectOne(chargingPointQueryWrapper);
            String position = chargingPoint.getPosition();

            bill.put("location", position);
            result.add(bill);
        }
        return GlobalResult.build(200, null, result);
    }

    /**
    * @Description: 修改支付时银行卡的支付顺序
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:58
    */
    @RequestMapping("/change_card_priority")
    public void changeCardPriority(
            @RequestParam("NewCardsPriority") String cardsString,
            @RequestParam("AccountId") String accountId
    ){
        System.out.println(cardsString);
        List<String> cardsNumber = Arrays.asList(cardsString.split(","));
        for (int i=0;i<cardsNumber.size();i++){
            String cardNumber = cardsNumber.get(i);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("accountId", accountId);
            wrapper.eq("cardNumber", cardNumber);
            Card card = this.cardMapper.selectOne(wrapper);
            card.setPriority(i);
            this.cardMapper.update(card,wrapper);
        }
    }

    /**
    * @Description: 获取当前银行卡中最低的优先级
    * @Author: ZMD
    * @UpdateTime: 2019/8/16 18:58
    */
    public int getLowestPriority(String accountId){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.orderByAsc("priority");
        wrapper.eq("accountId", accountId);
        List<Card> cards = this.cardMapper.selectList(wrapper);
        if (cards.size() == 0){
            return 0;
        }
        return cards.get(cards.size()-1).getPriority();
    }

    public double getDistanceTime(Date startTime, Date endTime) {
        double hour = 0;
        long time1 = startTime.getTime();
        long time2 = endTime.getTime();

        long diff;
        if (time1 < time2) {
            diff = time2 - time1;
        } else {
            diff = time1 - time2;
        }
        hour = (diff / (60 * 60 * 1000));
        return hour;
    }

}



