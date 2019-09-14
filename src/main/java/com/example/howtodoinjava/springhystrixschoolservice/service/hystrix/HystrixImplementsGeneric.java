package com.example.howtodoinjava.springhystrixschoolservice.service.hystrix;



public interface HystrixImplementsGeneric {
     String executeRequest(String url, String group, String command, String fallbackMessage);
}
