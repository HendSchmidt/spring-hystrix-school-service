package com.example.howtodoinjava.springhystrixschoolservice.service.hystrix;

import com.netflix.hystrix.*;
import com.netflix.hystrix.metric.consumer.HystrixDashboardStream;
import com.netflix.hystrix.strategy.eventnotifier.HystrixEventNotifierDefault;
import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

public class HystrixCustomCommand extends HystrixCommand<String> {

    private RestTemplate restTemplate;
    private String url;
    private String fallbackURL;

    public HystrixCustomCommand(String groupKey, String commandKey, RestTemplate restTemplate, String url, String fallbackURL){
        super(setter(groupKey, commandKey));
        this.restTemplate = restTemplate;
        this.url = url;
        this.fallbackURL = fallbackURL;
    }

    private static Setter setter(String groupKey, String commandKey) {
        HystrixCommandGroupKey group = HystrixCommandGroupKey.Factory.asKey(groupKey);
        HystrixCommandKey command = HystrixCommandKey.Factory.asKey(commandKey);

        /*#################CONFIGURAÇÂO PARA THREADS###########################*/
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(10).withMaximumSize(10).withMaxQueueSize(3);

        /*################CONFIGURAÇÂO CircuitBreaker and fallback#############*/
        HystrixCommandProperties.Setter commandproperty =  HystrixCommandProperties.Setter()
                .withFallbackEnabled(true)// habilita fallBack
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)// define a estrategia de isolar por threds
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(10)// seta o tempo de parada para a thread
                .withCircuitBreakerEnabled(true)//habiolita o circuit breaker
                .withCircuitBreakerRequestVolumeThreshold(10)//quantidade minima de requests que ira iniciar a abertura do circuito
                .withCircuitBreakerSleepWindowInMilliseconds(120000)// tempo que o circuito ira ficar aberto
                .withMetricsRollingStatisticalWindowInMilliseconds(20000)// config das estatisticas
                .withMetricsRollingPercentileWindowInMilliseconds(70000);// config das estatisticas

        return HystrixCommand.Setter
                .withGroupKey(group)//seta o atributo dinamico de grupo usado para agrupar as threads as threads
                .andCommandKey(command)//seta o atributo dinamico de comandoKey usado para criar as threads
                .andCommandPropertiesDefaults(commandproperty)
                .andThreadPoolPropertiesDefaults(threadPoolProperties);// seta os comando nas properties do hystrix
    }

    @Override
    protected String run() throws Exception {
        String response = restTemplate.getForObject(url, String.class);
        System.out.println("Response Received as " + response + " -  " + new Date());

        return "NORMAL FLOW "+ this.commandKey +" SERVICE!!! :::  " + response + " -  " + new Date();
    }

    @Override
    protected String getFallback() {
        String response;
        try {
            response = restTemplate.getForObject(url, String.class);
            System.out.println("Response Received as " + response + " -  " + new Date());

        }catch (Exception fallback){
            return "ALTERNATIVE FLOW "+ this.commandKey +" SERVICE!!! :::" + new Date();
        }

        return "ALTERNATIVE FLOW "+ this.commandKey +" SERVICE!!! :::  " + response + " -  " + new Date();
    }

}
