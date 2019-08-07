package com.example.demo222.repository;

import com.example.demo222.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface BindingCar extends JpaRepository<Car, Integer>, JpaSpecificationExecutor<Car> {


}
