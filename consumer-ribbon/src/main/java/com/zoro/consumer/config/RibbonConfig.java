package com.zoro.consumer.config;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.netflix.loadbalancer.IRule;

@Configuration
//@RibbonClient(name="provider-server", configuration = RibbonConfig.class)
public class RibbonConfig {

	@Bean
	public IRule myRule() {
		return new MyRule();
	}
}
