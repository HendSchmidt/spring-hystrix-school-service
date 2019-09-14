package com.example.howtodoinjava.springhystrixschoolservice.service.hystrix;

import com.netflix.hystrix.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

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
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter().withAllowMaximumSizeToDivergeFromCoreSize(true)
                .withCoreSize(240).withMaximumSize(800).withMaxQueueSize(120);

        /*################CONFIGURAÇÂO CircuitBreaker and fallback#############*/
        HystrixCommandProperties.Setter commandproperty =  HystrixCommandProperties.Setter()
                .withFallbackEnabled(true)// habilita fallBack
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)// define a estrategia de isolar por threds
                .withExecutionIsolationThreadInterruptOnTimeout(true)
                .withExecutionTimeoutInMilliseconds(1000)// seta o tempo de parada para a thread
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
        return "NORMAL FLOW "+ this.commandKey +" SERVICE!!! :::  " + response + " -  " + new Date();
    }

    @Override
    protected String getFallback() {
        String response;
        try {
            response = restTemplate.getForObject(fallbackURL, String.class);
        }catch (Exception fallback) {
            return "ALTERNATIVE FLOW "+ this.commandKey +" SERVICE!!! ::: WAS NOT REDIRECTED TO FALLBACKURL " + new Date();
        }

        return "ALTERNATIVE FLOW "+ this.commandKey +" SERVICE!!! ::: " + response + " -  " + new Date();
    }

}
