package stream.receive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * 实现对消息通道（Channel）的绑定
 * 我们通过@EnableBinding(Sink.class)绑定了Sink接口，该接口是Spring Cloud Stream中默认实现的对输入消息通道绑定的定义
 * @author 67zhong
 *
 */
@EnableBinding(Sink.class)
public class SinkReceiver {
	
	private static Logger logger = LoggerFactory.getLogger(SinkReceiver.class);

	@StreamListener(Sink.INPUT)
	public void receive(Object payload) {
        logger.info("Received: " + payload);
	}

}
