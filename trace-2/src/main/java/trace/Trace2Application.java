package trace;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class Trace2Application {

	public static void main(String[] args) {
		SpringApplication.run(Trace2Application.class, args);
	}
	
	
	private static Logger logger = LoggerFactory.getLogger(Trace2Application.class);
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@GetMapping("/trace-2")
	public String trace2(HttpServletRequest request) {
		logger.info("---- call trace2, TraceId={}, SpanID={} -----", 
				request.getHeader("X-B3-TraceId"),
				request.getHeader("X-B3-SpanId"));
		
		return "Trace end";
	}

}
