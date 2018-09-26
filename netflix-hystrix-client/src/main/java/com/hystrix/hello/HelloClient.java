package com.hystrix.hello;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HelloClient {

	public static void main(String[] args) throws ClientProtocolException, IOException {
		helloCommand();
		httpClient();
	}
	
	private static void httpClient() throws ClientProtocolException, IOException  {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:8090/test/consumer/1");
		CloseableHttpResponse response = httpClient.execute(httpGet);
		HttpEntity entity = response.getEntity();
		System.out.println(EntityUtils.toString(entity));
	}
	
	private static void helloCommand() {
		HelloCommand command = new HelloCommand();
		String execute = command.execute();
		System.out.println(execute);
	}

}
