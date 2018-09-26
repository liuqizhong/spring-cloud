package netflix.ribbon.test;

import java.net.URI;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.niws.client.http.RestClient;

import netflix.ribbon.use.MyRule;

/**
 * 1. 手动的维护各个服务实例(静态的服务注册与发现/在配置文件中写死)：若服务变更，则需手动的去修改配置文件(服务列表)，
 * 	      若某些服务器宕机了，客户端也不知道，会有可能用到已经宕机掉的服务实例
 * 
 * 导致这样的问题的根本就是服务提供者和服务消费者之间紧耦合
 * 
 * 2. 在服务提供者和服务消费者之间引入注册中心(Zookeeper或Eureka), 动态的服务注册和发现, 可解决上面问题
 * @author 67zhong
 *
 */
public class NetFlixRibbonTest {

	public static void main(String[] args) throws Exception {
		ConfigurationManager.loadPropertiesFromResources("my-client.properties");
		//设置自定义路由规则 
		ConfigurationManager.getConfigInstance().setProperty("my-client.NFLoadBalancerRuleClassName", MyRule.class.getName());
		
		 RestClient client = (RestClient) ClientFactory.getNamedClient("my-client"); 
		 HttpRequest request = HttpRequest.newBuilder().uri(new URI("/hello/message/1")).build(); // 3
	      for (int i = 0; i < 20; i++)  {
	      	HttpResponse response = client.executeWithLoadBalancer(request); // 4
	      	String entity = response.getEntity(String.class);
	      	System.out.println(entity);
	      }
	}

}
