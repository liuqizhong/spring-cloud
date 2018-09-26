package com.zoro.feign.servers;

import java.util.Map;

import com.zoro.feign.bean.Person;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

public interface ProviderServer {
	
	@RequestLine("GET /hello/message/{id}")
	public Map<String, Object> message(@Param("id") String id);
	
	@RequestLine("POST /hello/person/create")
	@Headers("Content-Type: application/json")
	public String person(Person p);

}
