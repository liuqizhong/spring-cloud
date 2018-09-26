package com.zoro.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zoro.consumer.service.HelloService;

@Controller
@RequestMapping("/test/")
public class HelloController {
	
	@Autowired
	private HelloService helloService;
	
	@GetMapping("consumer/{id}")
	@ResponseBody
	public String consumer(@PathVariable("id") String id) {
		String json = helloService.hello(id);
		//测试hystrix缓存
		for(int i = 0; i < 3; i++) {
			helloService.hello(id);
		}
		return json;
	}
	
	@GetMapping("getCache/{id}")
	@ResponseBody
	public String getCache(@PathVariable("id") String id) {
		/**
		 * hystrix cache 要求在同一请求中
		 */
		//这里连续调用两次，但只会输出 一次，因为第二次被缓存了
		String cache = helloService.getCache(id);
		helloService.getCache(id);
		System.out.println("### 删除缓存 ###");
		helloService.cacheRemove(id);
		//缓存被清空，接着输出
		helloService.getCache(id);
		return cache;
	}
	
	/**
	 * hystrix 合并请求测试
	 * @param id
	 * @return
	 */
	@GetMapping("testColl/{id}")
	@ResponseBody
	public String testColl(@PathVariable("id") Integer id) {
		helloService.testColl(id + "");
		helloService.testColl(++id + "");
		helloService.testColl(++id + "");
		return "success";
	}
}
