package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * 测试超时回退
 * 
 * @author 67zhong
 *
 */
public class TimeoutCommand extends HystrixCommand<String> {

	protected TimeoutCommand() {
		//局部配置，会覆盖全局配置
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TimeoutGroup"))
				.andCommandPropertiesDefaults(
						// 设置超时时间
						HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)));
	}

	@Override
	protected String run() throws Exception {
		Thread.sleep(2000);
		System.out.println("执行命令");
		return "success";
	}

	@Override
	protected String getFallback() {
		System.out.println("执行回退方法");
		return "fallback";
	}

	public static void main(String[] args) {
		// 全局配置
		ConfigurationManager.getConfigInstance()
				.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 3000);

		TimeoutCommand tc = new TimeoutCommand();
		String result = tc.execute();

		System.out.println(result);
	}

}
