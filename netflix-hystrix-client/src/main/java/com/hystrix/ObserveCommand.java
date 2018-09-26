package com.hystrix;

import rx.Observable;
import rx.Observer;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class ObserveCommand {

	public static void main(String[] args) throws Exception {
		RunCommand c1 = new RunCommand("observe method");
		c1.observe();
		
		RunCommand c2 = new RunCommand("toObservable method");
		Observable ob = c2.toObservable();
		ob.subscribe(new Observer<String>() {

			public void onCompleted() {
				System.out.println("command completed");
			}

			public void onError(Throwable e) {
				
			}

			public void onNext(String t) {
				System.out.println("###" + t);
			}
			
		});
		
		Thread.sleep(1000);
	}

	static class RunCommand extends HystrixCommand<String> {
		
		String msg;
		
		public RunCommand(String msg) {
			super(HystrixCommandGroupKey.Factory.asKey("TestGroup"));
			this.msg = msg;
		}

		@Override
		protected String run() throws Exception {
			System.out.println(msg);
			return "success";
		}
	}
}
