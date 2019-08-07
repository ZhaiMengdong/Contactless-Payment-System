package com.example.demo222.repository;

import com.example.demo222.entity.ReqUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by 夏路遥
 * 8/1/2019 11:25 AM
 */

@Component
public interface MyRepository extends JpaRepository<ReqUser,Integer>, JpaSpecificationExecutor<ReqUser>{

    //登陆
    @Query(value = "select * from cardbinding  where accountName=?1 and txSnBinding=?2",nativeQuery = true)
    List<ReqUser> findByAccountnameAndTxSnBinding(String accountName, String txSnBinding);

    List<ReqUser> findAll();


    //注册
    @Query(value = "select * from cardbinding  where txSnBinding=?1", nativeQuery = true)

          List<ReqUser> findByTxSnBinding(String txSnBinding);

    @Query(value = "select txSnBinding from cardbinding where accountName=?1", nativeQuery = true)

        String findTxSNBindingByAccountNumber(String AccountNumber);
}
