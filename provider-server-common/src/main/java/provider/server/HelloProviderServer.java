package provider.server;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import provider.bean.Person;

/**
 * feign客户端可实现该接口
 * @author 67zhong
 *
 */
public interface HelloProviderServer {
	
	@GetMapping(value="/hello/message/{id}")
	public Map<String, Object> message(@PathVariable("id") String id);

	@PostMapping("/hello/person/create")
	public String person(@RequestBody Person p);
}
