package com.zoro.consumer.config;

import java.util.List;
import java.util.Random;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;

public class MyRule implements IRule {
	
	private ILoadBalancer lb;
	
	public MyRule() {}
	
	public MyRule(ILoadBalancer lb) {
		setLoadBalancer(lb);
	}

	@Override
	public Server choose(Object key) {
		Random ran = new Random();
		int nextInt = ran.nextInt(10);
		
		List<Server> servers = lb.getAllServers();
		
		if(nextInt > 7) {
			return getServerByPort(servers, 8081);
		}
		
		return getServerByPort(servers, 8080);
	}
	
	private Server getServerByPort(List<Server> servers, int port) {
		for(Server s: servers) {
			if(s.getPort() == port) {
				return s;
			}
		}
		return null;
	}

	@Override
	public void setLoadBalancer(ILoadBalancer lb) {
		this.lb = lb;
	}

	@Override
	public ILoadBalancer getLoadBalancer() {
		return this.lb;
	}

}
