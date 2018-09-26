package com.zoro.feign;

import java.util.Map;

import com.zoro.feign.servers.ProviderServer;

import feign.Feign;
import feign.gson.GsonDecoder;

public class HelloFeign {

	public static void main(String[] args) {
		ProviderServer ps = Feign.builder()
				.decoder(new GsonDecoder()) // ½âÂëÆ÷
				.target(ProviderServer.class, "http://192.168.0.102:8080");
		Map<String, Object> map = ps.message(1 + "");
		System.out.println(map);
		
	}

}
