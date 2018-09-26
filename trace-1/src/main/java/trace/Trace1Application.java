package trace;

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
public class Trace1Application {

	public static void main(String[] args) {
		SpringApplication.run(Trace1Application.class, args);
	}
	
	
	private static Logger logger = LoggerFactory.getLogger(Trace1Application.class);
	
	@LoadBalanced
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@GetMapping("/trace-1")
	public String trace1() {
		logger.info("---- call trace1 -----");
		return restTemplate().getForEntity("http://trace-2/trace-2", String.class).getBody();
	}

}
