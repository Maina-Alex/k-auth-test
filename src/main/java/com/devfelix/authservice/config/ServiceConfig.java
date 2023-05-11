package com.devfelix.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;

@Configuration
public class ServiceConfig {

    @Bean
    public PasswordEncoder provideEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public KeyPair  generateKeys() throws Exception{
       KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }


}
