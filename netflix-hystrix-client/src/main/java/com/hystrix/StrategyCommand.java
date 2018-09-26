package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolProperties;

/**
 * ������ԣ�1.�̳߳�(Ĭ��)  2.�ź���
 * 
 * @author 67zhong
 *
 */
public class StrategyCommand extends HystrixCommand<String>{

	private int index;
	
	protected StrategyCommand(int index) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("threadCommand"))
				// �����̳߳صĴ�С
				.andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3))
				);
		this.index = index;
	}
	
	@Override
	protected String run() throws Exception {
		Thread.sleep(500);
		System.out.println("ִ�з���. ��ǰ����:"+this.index);
		return "success";
	}
	
	@Override
	protected String getFallback() {
		System.out.println("���˷���. ��ǰ����:"+this.index);
		return "fallback";
	}

	public static void main(String[] args) {
		//1. ��ʾ�̲߳���
		/*for(int i = 0; i < 6; i++) {
			ThreadCommand tc = new ThreadCommand(i);
			tc.queue();
		}*/
		
		//2. ��ʾ�ź���
		//ͨ�����ã������Ըĳ��ź���
		ConfigurationManager.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.strategy", ExecutionIsolationStrategy.SEMAPHORE);
		//�ź�����СΪ3
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
