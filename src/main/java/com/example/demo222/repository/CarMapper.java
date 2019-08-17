package com.example.demo222.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.demo222.entity.Car;

import org.springframework.stereotype.Component;

//public interface BindingCar extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {
//
//
//}
@Component
public interface CarMapper extends BaseMapper<Car>{

}