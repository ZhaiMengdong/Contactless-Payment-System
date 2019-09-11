package com.example.demo222;

import com.example.demo222.service.CallBack;
import com.example.demo222.service.MqttServer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@MapperScan("com.example.demo222.repository") //设置mapper接口的扫描包
@SpringBootApplication
public class Demo222Application {

    public static void main(String[] args) {
        SpringApplication.run(Demo222Application.class, args);
        MqttServer mqttServer = new MqttServer();
        mqttServer.start();
    }

}
