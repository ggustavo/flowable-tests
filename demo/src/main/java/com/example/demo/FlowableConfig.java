/*
package com.example.demo;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

 // @Configuration
public class FlowableConfig {
	
	@Primary
    @Bean
    public SpringAsyncExecutor springAsyncExecutor() {
        SpringAsyncExecutor asyncExecutor = new SpringAsyncExecutor();
        
        return asyncExecutor;
    }
    
    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(SpringAsyncExecutor asyncExecutor) {
        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setAsyncExecutor(asyncExecutor);
        configuration.setAsyncExecutorActivate(true);
        System.out.println("---------------------------------------------------------------------------------------------------");
		System.out.println("Enable Executor!!!!");
		System.out.println("---------------------------------------------------------------------------------------------------");
        return configuration;
    }
  

}
*/