package com.zoro.feign;

import com.zoro.feign.bean.Person;
import com.zoro.feign.servers.ProviderServer;

import feign.Feign;
import feign.gson.GsonEncoder;

public class JsonTest {

	public static void main(String[] args) {
		ProviderServer ps = Feign.builder()
		.encoder(new GsonEncoder()) //±àÂëÆ÷
		.target(ProviderServer.class, "http://192.168.0.102:8080");
		
		Person p = new Person();
		p.setId(1+"");
		p.setName("Ð¡³Â");
		
		String result = ps.person(p);
		System.out.println(result);
	}

}
