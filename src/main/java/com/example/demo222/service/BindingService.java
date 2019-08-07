package com.example.demo222.service;

import com.example.demo222.entity.Car;
import com.example.demo222.entity.Card;

import java.util.List;

public interface BindingService {

    void saveCar(Car car);

    void saveCard(Card card);

    List<Card> findByTxSNBinding(String txSNBinding);
}
