package consumer.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.netflix.hystrix.HystrixCircuitBreaker;
import com.netflix.hystrix.HystrixCommandKey;

import consumer.server.ProviderServer;
import provider.bean.Person;

@Controller
@RequestMapping("/feign/")
public class FeignTestController {
	
	@Autowired
	private ProviderServer providerServer;
	
	@GetMapping("/message/{id}")
	@ResponseBody
	public Map<String, Object> message(@PathVariable String id) {
		Map<String, Object> message = providerServer.message(id);
		HystrixCircuitBreaker breaker = HystrixCircuitBreaker.Factory
				.getInstance(HystrixCommandKey.Factory.asKey("ProviderServer#message(String)"));
		System.out.println("¶ÏÂ·Æ÷×´Ì¬£º" + breaker.isOpen());
		return message;
	}
	
	@GetMapping("/person/{id}/{name}")
	@ResponseBody
	public String person(@PathVariable String id, @PathVariable String name) {
		Person p = new Person(); 
		p.setId(id);
		p.setName(name);
		String person = providerServer.person(p);
		return person;
	}

}
