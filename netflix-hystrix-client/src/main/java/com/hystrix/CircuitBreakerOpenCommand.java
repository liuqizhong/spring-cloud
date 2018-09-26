package com.hystrix;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandMetrics.HealthCounts;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * ��·������
 * 
 * һ��
 * ��·����������������������������������·���ͻ�򿪡�
 * 
 * 1. ��10���ڣ����������ﵽ20��(������������Ĭ��Ϊ20��)
 * 2. �����ʧ���ʳ���50%
 * 
 * ����
 * ��·����֮�󣬴��������ڣ����������ڼ䣬���е�����ֱ���߻��˷���(��ʵ���е����󶼻����ж϶�·���Ƿ�򿪣�����ֱ���߻��˷���)
 * ������ʱ��Ĭ�������룬����������֮���������������������ɹ����ͻὫ��·���رգ������ֽ��������ڡ�
 * 
 * @author 67zhong
 *
 */
public class CircuitBreakerOpenCommand extends HystrixCommand<String> {

	protected CircuitBreakerOpenCommand() {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CircuitBreaker")).
				andCommandPropertiesDefaults(
						//��10���ڣ�����10�����������һ������
				HystrixCommandProperties.Setter().withCircuitBreakerRequestVolumeThreshold(10)));
	}

	@Override
	protected String run() throws Exception {
		Thread.sleep(200);
		return "success";
	}
	
	@Override
	protected String getFallback() {
		return "fallback";
	}

	public static void main(String[] args) throws InterruptedException {
		//����һ. ����ȫ�����ã�ģ�ⳬʱ
		ConfigurationManager
				.getConfigInstance()
				.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 100);
		System.out.println("-----------����һ��ʼ-----------");
		for(int i = 0 ; i < 20 ; i++) {
			CircuitBreakerOpenCommand coc = new CircuitBreakerOpenCommand();
			coc.execute();
			// �����·��״̬
			// Ԥ�ڣ���10��ǰ�����·������δ�򿪣���11����·���Ѵ򿪣�����������ȫ���߻��˷���
			System.out.println(coc.isCircuitBreakerOpen());
		}
		System.out.println("-----------����һ����-----------");
		//������ͣ��6�룬��·���ڽ���������ʱ�᳢���Եķ���һ�����󣬵����ǻᳬʱ�����Զ�·���ִ��ڴ�״̬
		//���������������ó�ʱʱ�䣬������·���ڽ���������ʱ�᳢���Եķ���һ�����󣬾Ͳ��ᳬʱ�ˣ���·���ͻᴦ�ڹر�״̬
		ConfigurationManager
			.getConfigInstance()
			.setProperty("hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds", 300);
		
		Thread.sleep(6000);
		
		System.out.println("-----------���Զ���ʼ-----------");
		//���Զ�
		for(int i = 0 ; i < 20 ; i++) {
			CircuitBreakerOpenCommand coc = new CircuitBreakerOpenCommand();
			coc.execute();
			HealthCounts healthCounts = coc.getMetrics().getHealthCounts();
			System.out.println("��·��״̬: "+coc.isCircuitBreakerOpen()+", ����������"+healthCounts.getTotalRequests());
			if(coc.isCircuitBreakerOpen()) {
				System.out.println("####��·�����ˣ�����������#####");
				Thread.sleep(5000);
				System.out.println("####�����ڽ���#####");
			}
		}
	}

}
