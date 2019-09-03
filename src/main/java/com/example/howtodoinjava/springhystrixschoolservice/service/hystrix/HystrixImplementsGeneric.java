package com.example.howtodoinjava.springhystrixschoolservice.service.hystrix;



public interface HystrixImplementsGeneric {
     String excuteRequest (String url, String group, String command, String fallbackMessage);
}
