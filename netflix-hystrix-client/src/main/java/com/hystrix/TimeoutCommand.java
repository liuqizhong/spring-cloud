package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * ���Գ�ʱ����
 * 
 * @author 67zhong
 *
 */
public class TimeoutCommand extends HystrixCommand<String> {

	protected TimeoutCommand() {
		//�ֲ����ã��Ḳ��ȫ������
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("TimeoutGroup"))
				.andCommandPropertiesDefaults(
						// ���ó�ʱʱ��
						HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)));
	}

	@Override
	protected String run() throws Exception {
		Thread.sleep(2000);
		System.out.println("ִ������");
		return "success";
	}

	@Override
	protected String getFallback() {
		System.out.println("ִ�л��˷���");
		return "fallback";
	}

	public static void main(String[] args) {
		// ȫ������
		ConfigurationManager.getConfigInstance()
				.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 3000);

		TimeoutCommand tc = new TimeoutCommand();
		String result = tc.execute();

		System.out.println(result);
	}

}
