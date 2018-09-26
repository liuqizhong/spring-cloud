package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * 隔离策略：1.线程池(默认)  2.信号量
 * 
 * @author 67zhong
 *
 */
public class StrategyCommand extends HystrixCommand<String>{

	private int index;
	
	protected StrategyCommand(int index) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("threadCommand"))
				// 设置线程池的大小
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3))
				);
		this.index = index;
	}
	
	@Override
	protected String run() throws Exception {
		Thread.sleep(500);
		System.out.println("执行方法. 当前索引:"+this.index);
		return "success";
	}
	
	@Override
	protected String getFallback() {
		System.out.println("回退方法. 当前索引:"+this.index);
		return "fallback";
	}

	public static void main(String[] args) {
		//1. 演示线程策略
		/*for(int i = 0; i < 6; i++) {
			ThreadCommand tc = new ThreadCommand(i);
			tc.queue();
		}*/
		
		//2. 演示信号量
		//通过配置，将策略改成信号量
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.strategy", ExecutionIsolationStrategy.SEMAPHORE);
		//信号量大小为3
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.semaphore.maxConcurrentRequests", 3);
		for(int i = 0; i < 6; i++) {
			final int index = i;
			new Thread(new Runnable() {
				@Override
				public void run() {
					StrategyCommand tc = new StrategyCommand(index);
					tc.execute();
				}
			}).start();;
		}
	}

}
