package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandMetrics.HealthCounts;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 断路器测试
 * 
 * 一：
 * 断路器打开有两个条件，满足以下两个条件断路器就会打开。
 * 
 * 1. 在10秒内，请求数量达到20个(可配置数量，默认为20个)
 * 2. 请求的失败率超过50%
 * 
 * 二：
 * 断路器打开之后，处于休眠期，在休眠期期间，所有的请求都直接走回退方法(其实所有的请求都会先判断断路器是否打开，打开了直接走回退方法)
 * 休眠期时长默认是五秒，过了休眠期之后，再有请求过来，若请求成功，就会将断路器关闭，否则又进入休眠期。
 * 
 * @author 67zhong
 *
 */
public class CircuitBreakerOpenCommand extends HystrixCommand<String> {

	protected CircuitBreakerOpenCommand() {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CircuitBreaker")).
				andCommandPropertiesDefaults(
						//在10秒内，大于10个请求满足第一个条件
				HystrixCommandProperties.Setter().withCircuitBreakerRequestVolumeThreshold(10)));
	}

	@Override
	protected String run() throws Exception {
		Thread.sleep(200);
		return "success";
	}
	
	@Override
	protected String getFallback() {
		return "fallback";
	}

	public static void main(String[] args) throws InterruptedException {
		//测试一. 利用全局配置，模拟超时
		ConfigurationManager
				.getConfigInstance()
				.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 100);
		System.out.println("-----------测试一开始-----------");
		for(int i = 0 ; i < 20 ; i++) {
			CircuitBreakerOpenCommand coc = new CircuitBreakerOpenCommand();
			coc.execute();
			// 输出断路器状态
			// 预期：第10秒前请求断路器都是未打开，第11秒后断路器已打开，后续的请求全部走回退方法
			System.out.println(coc.isCircuitBreakerOpen());
		}
		System.out.println("-----------测试一结束-----------");
		//在这里停留6秒，断路器在结束休眠期时会尝试性的发送一次请求，但还是会超时，所以断路器又处于打开状态
		//所以立即重新设置超时时间，这样断路器在结束休眠期时会尝试性的发送一次请求，就不会超时了，断路器就会处于关闭状态
		ConfigurationManager
			.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 300);
		
		Thread.sleep(6000);
		
		System.out.println("-----------测试二开始-----------");
		//测试二
		for(int i = 0 ; i < 20 ; i++) {
			CircuitBreakerOpenCommand coc = new CircuitBreakerOpenCommand();
			coc.execute();
			HealthCounts healthCounts = coc.getMetrics().getHealthCounts();
			System.out.println("断路器状态: "+coc.isCircuitBreakerOpen()+", 请求数量："+healthCounts.getTotalRequests());
			if(coc.isCircuitBreakerOpen()) {
				System.out.println("####断路器打开了，进入休眠期#####");
				Thread.sleep(5000);
				System.out.println("####休眠期结束#####");
			}
		}
	}

}
