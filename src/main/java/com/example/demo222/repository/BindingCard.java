package com.example.demo222.repository;

import com.example.demo222.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BindingCard extends JpaRepository<Card, Integer>, JpaSpecificationExecutor<Card> {

    @Query(value = "select * from cards where txSNBinding=?1", nativeQuery = true)
    List<Card> findByTxSNBinding(String txSNBinding);
}
