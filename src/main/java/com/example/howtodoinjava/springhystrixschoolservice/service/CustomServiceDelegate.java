package com.example.howtodoinjava.springhystrixschoolservice.service;

import com.example.howtodoinjava.springhystrixschoolservice.service.hystrix.HystrixCustomCommand;
import com.example.howtodoinjava.springhystrixschoolservice.service.hystrix.HystrixImplementsGeneric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomServiceDelegate implements HystrixImplementsGeneric {
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public String excuteRequest(String url, String group, String command, String fallbackURL) {
        HystrixCustomCommand hystrixCustomCommand = new HystrixCustomCommand(group, command, restTemplate, url, fallbackURL);
        return hystrixCustomCommand.execute();
    }
}
