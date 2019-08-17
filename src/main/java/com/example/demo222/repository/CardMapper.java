package com.example.demo222.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo222.entity.Card;

import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//public interface BindingCard extends JpaRepository<Card, Integer>, JpaSpecificationExecutor<Card> {
//
//    @Query(value = "select * from card where txSnBinding=?1", nativeQuery = true)
//    List<Card> findByTxSNBinding(String txSNBinding);
//
//    @Query(value = "select * from card where cardNumber=?1", nativeQuery = true)
//    List<Card> findTxSNBindingByCardNumber(String cardNumber);
//}
@Component
public interface CardMapper extends BaseMapper<Card>{

}