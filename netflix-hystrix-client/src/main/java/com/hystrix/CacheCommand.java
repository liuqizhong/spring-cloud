package com.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixRequestCache;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

/**
 * ���Ի���
 * 
 * Ҫ��һ���������������
 * @author 67zhong
 *
 */
public class CacheCommand extends HystrixCommand<String>{
	
	private String cacheKey;

	protected CacheCommand(String cacheKey) {
		super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("CacheCommand"))
				.andCommandKey(HystrixCommandKey.Factory.asKey("myCache")));
		this.cacheKey = cacheKey;
	}
	
	@Override
	protected String run() throws Exception {
		System.out.println("ִ�з���");
		return "hello cache";
	}

	@Override
	protected String getCacheKey() {
		return this.cacheKey;
	}

	public static void main(String[] args) {
		HystrixRequestContext hrc = HystrixRequestContext.initializeContext();
		String cacheKey = "hello key";
		CacheCommand c1 = new CacheCommand(cacheKey);
		CacheCommand c2 = new CacheCommand(cacheKey);
		CacheCommand c3 = new CacheCommand(cacheKey);
		
		c1.execute();
		c2.execute();
		c3.execute();
		
		System.out.println("����c1, �Ƿ��ȡ���棺"+c1.isResponseFromCache);
		System.out.println("����c2, �Ƿ��ȡ���棺"+c2.isResponseFromCache);
		System.out.println("����c3, �Ƿ��ȡ���棺"+c3.isResponseFromCache);
		
		//��ջ���
		HystrixRequestCache cache = HystrixRequestCache
					.getInstance(HystrixCommandKey.Factory.asKey("myCache"), HystrixConcurrencyStrategyDefault.getInstance());
		cache.clear(cacheKey);
		
		CacheCommand c4 = new CacheCommand(cacheKey);
		c4.execute();
		System.out.println("����c4, �Ƿ��ȡ���棺"+c4.isResponseFromCache);
		
		hrc.shutdown();
	}
}
