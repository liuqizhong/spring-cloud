package netflix.ribbon.use;

import java.util.ArrayList;
import java.util.List;

import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;

public class LBMain {

	public static void main(String[] args) {
		BaseLoadBalancer lb = new BaseLoadBalancer();
		//设置负载均衡路由规则
		lb.setRule(new MyRule(lb));
		
		List<Server> servers = new ArrayList<>();	
		servers.add(new Server("localhost", 8080));
		servers.add(new Server("localhost", 8081));
		
		lb.addServers(servers);
		
		for(int i = 0 ; i < 10 ; i++) {
			Server server = lb.chooseServer();
			System.out.println(server);
		}
		
	}

}
