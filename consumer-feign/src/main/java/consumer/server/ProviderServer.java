package consumer.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;

import provider.bean.Person;
import provider.server.HelloProviderServer;

@FeignClient(name = "provider-server", fallback = ProviderServerFallback.class)
public interface ProviderServer extends HelloProviderServer{
	
}

@Component
class ProviderServerFallback implements ProviderServer{

	@Override
	public Map<String, Object> message(String id) {
		Map<String, Object> result = new HashMap<>();
		result.put("errMsg", "����ʧ��, ִ�л��˷���");
		return result;
	}

	@Override
	public String person(Person p) {
		return "error msg: ����ʧ��, ִ�л��˷���";
	}
	
}
