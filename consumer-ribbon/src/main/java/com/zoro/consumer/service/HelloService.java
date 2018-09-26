package com.zoro.consumer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;

@Service
//hystrix:全局配置
//@DefaultProperties()
public class HelloService {
	
	//封装了客户端负载均衡框架Ribbon
	@Autowired
	private RestTemplate restTemplate;
	
	//hystrix:局部配置
	@HystrixCommand(fallbackMethod = "helloFallBackMethod", groupKey = "HelloHystrixGruop", commandKey = "TestCacheKey",
				commandProperties = {
						@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")
				},
				threadPoolProperties = {
						@HystrixProperty(name = "coreSize", value = "3")
				}
			)
	/**
	 * 开启hystrix缓存， 和@HystrixCommand一起使用，且在同一次请求中，注意在MyFilter中的配置，如无MyFilter中的配置则会报错
	 * 能使用缓存的前提：在同一请求中多次调用同一个命令，结果就会被缓存起来
	 */
	@CacheResult
	public String hello(String id) {
		System.out.println("远程调用中...");
		String json = restTemplate.getForObject("http://provider-server/hello/message/" + id, String.class);
		return json;
	}
	
	/**
	 * 回退方法, 注意形参要和hello方法一致
	 * @return
	 */
	public String helloFallBackMethod(String id) {
		return "error";
	}
	
	
	@CacheResult
	@HystrixCommand(commandKey = "TestCacheKey")
	public String getCache(String id) {
		System.out.println("执行查询方法");
		return "test hystrix cache";
	}
	
	@CacheRemove(commandKey = "TestCacheKey")
	@HystrixCommand
	public void cacheRemove(String id) {
		System.out.println("清空缓存");
	}
	
	
	/**
	 * 请求合并测试
	 * 注意：返回值是String, 则合并方法testColls的返回值应该要是List<String>
	 * @param id
	 */
	@HystrixCollapser(batchMethod = "testColls", 
			collapserProperties = {
					//合并1秒内的所有请求，合并后发给testColls方法
					@HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")
			}
	)
	public String testColl(String id) {
		System.out.println("请求合并...");
		return "";
	}
	
	@HystrixCommand
	public List<String> testColls(List<String> ids) {
		List<String> collList = new ArrayList<>();
		System.out.println("请求合并...");
		for(String id: ids) {
			System.out.println(id);
			collList.add(id);
		}
		return collList;
	}

}
