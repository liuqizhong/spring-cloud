package stream.sender;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.core.MessageSource;
import org.springframework.messaging.support.GenericMessage;

@EnableBinding(value = {Source.class})
public class Sender {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

	@Bean
    @InboundChannelAdapter(value = Source.OUTPUT)
    public MessageSource<String> timerMessageSource() {
		 return () -> {
	            Double value = Math.random() * 10 % 5;
	            int age = value.intValue();
	            LOGGER.info("current age : {}", age);
	            Map<String, Object> headers = new HashMap<>();
	            headers.put("router", age);
	            //age值将作为后续分区的标识；
	            //在消息中添加Header消息头，并设定router属性为age值，作为后续自定义分区Class实现的分区标识；
	            return new GenericMessage<>("{\"name\":\"didi\", \"age\":"+age+"}", headers);

		 };
	}

}
