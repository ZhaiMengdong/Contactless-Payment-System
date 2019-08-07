package com.example.demo222.service.impl;

import com.example.demo222.entity.Car;
import com.example.demo222.entity.Card;
import com.example.demo222.repository.BindingCar;
import com.example.demo222.repository.BindingCard;
import com.example.demo222.service.BindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zmd
 * @Date: 2019/8/6 17:24
 **/
@Service("bindingService")
public class BindingServiceImpl implements BindingService {
    @Autowired
    BindingCar bindingCar;

    @Autowired
    BindingCard bindingCard;

    @Override
    public void saveCar(Car car){
        bindingCar.save(car);
    }

    @Override
    public void saveCard(Card card) {
        bindingCard.save(card);
    }

    @Override
    public List<Card> findByTxSNBinding(String txSNBinding){
        return bindingCard.findByTxSNBinding(txSNBinding);
    }

}
