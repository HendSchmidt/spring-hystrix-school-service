package com.example.howtodoinjava.springhystrixschoolservice.service.hystrix;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

public class HystrixCustomCommand extends HystrixCommand<String> {

    private RestTemplate restTemplate;
    private String url;
    private String fallbackMessage;

    public HystrixCustomCommand(String groupKey, String commandKey, RestTemplate restTemplate, String url, String fallbackMessage){
        super(setter(groupKey, commandKey));
        this.restTemplate = restTemplate;
        this.url = url;
        this.fallbackMessage = fallbackMessage;
    }

    private static Setter setter(String groupKey, String commandKey) {
        HystrixCommandGroupKey group = HystrixCommandGroupKey.Factory.asKey(groupKey);
        HystrixCommandKey command = HystrixCommandKey.Factory.asKey(commandKey);
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("funniestThreadPoolKey");

        HystrixThreadPoolProperties.Setter threadproperties =
                HystrixThreadPoolProperties.Setter()
                    .withCoreSize(20)
                    .withKeepAliveTimeMinutes(1)
                    .withMaxQueueSize(1000)
                    .withQueueSizeRejectionThreshold(100);

        HystrixCommandProperties.Setter commandproperty =
                HystrixCommandProperties.Setter()
                        .withFallbackEnabled(true)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(1000);

        return HystrixCommand.Setter.withGroupKey(group)
                .andCommandKey(command)
                .andThreadPoolPropertiesDefaults(threadproperties)
                .andCommandPropertiesDefaults(commandproperty)
                .andThreadPoolKey(threadPoolKey);
    }

    @Override
    protected String run() throws Exception {
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response Received as " + response + " -  " + new Date());

        return "NORMAL FLOW "+ this.commandKey +" SERVICE!!! :::  " + response + " -  " + new Date();
    }

    @Override
    protected String getFallback() {
        return this.fallbackMessage + new Date();
    }

}
