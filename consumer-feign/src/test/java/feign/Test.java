package feign;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Test {
	
	/**
	 * ²âÊÔ¶ÏÂ·Æ÷
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	@org.junit.Test
	public void testCircuitBreaker() throws ClientProtocolException, IOException, InterruptedException {
		CloseableHttpClient createDefault = HttpClients.createDefault();
		String url = "http://localhost:8095/feign/message/1";
		
		for(int i = 0; i < 6; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						HttpGet http = new HttpGet(url);
						CloseableHttpResponse response = createDefault.execute(http);
						HttpEntity entity = response.getEntity();
						String result = EntityUtils.toString(entity);
						System.out.println(result);
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		
		Thread.sleep(15000);
		
	}

}
