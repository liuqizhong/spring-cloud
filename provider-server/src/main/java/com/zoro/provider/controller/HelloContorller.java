package com.zoro.provider.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import provider.bean.Person;
import provider.server.HelloProviderServer;

@RestController
public class HelloContorller implements HelloProviderServer{
	
	@Autowired
	private DiscoveryClient client;
	
	@Autowired
	private  HttpServletRequest request;
	
	@Override
	public Map<String, Object> message(@PathVariable("id") String id) {
		List<String> services = client.getServices();
		List<ServiceInstance> instances = client.getInstances("provider-server");
		String requestURL = request.getRequestURL().toString();
		
		System.out.println("services：" + services);
		System.out.println("instances：" + instances);
		
		Map<String, Object> result = new HashMap<>();
		result.put("id", id);
		result.put("url", requestURL);
		
		return result;
	}
	
	
	@Override
	public String person(@RequestBody Person p) {
		return "success:" + p.getId() + "," + p.getName();
	}
	
}
