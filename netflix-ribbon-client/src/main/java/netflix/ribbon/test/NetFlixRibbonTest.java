package netflix.ribbon.test;

import java.net.URI;

import com.netflix.client.ClientFactory;
import com.netflix.client.http.HttpRequest;
import com.netflix.client.http.HttpResponse;
import com.netflix.config.ConfigurationManager;
import com.netflix.niws.client.http.RestClient;

import netflix.ribbon.use.MyRule;

/**
 * 1. �ֶ���ά����������ʵ��(��̬�ķ���ע���뷢��/�������ļ���д��)�����������������ֶ���ȥ�޸������ļ�(�����б�)��
 * 	      ��ĳЩ������崻��ˣ��ͻ���Ҳ��֪�������п����õ��Ѿ�崻����ķ���ʵ��
 * 
 * ��������������ĸ������Ƿ����ṩ�ߺͷ���������֮������
 * 
 * 2. �ڷ����ṩ�ߺͷ���������֮������ע������(Zookeeper��Eureka), ��̬�ķ���ע��ͷ���, �ɽ����������
 * @author 67zhong
 *
 */
public class NetFlixRibbonTest {

	public static void main(String[] args) throws Exception {
		ConfigurationManager.loadPropertiesFromResources("my-client.properties");
		//�����Զ���·�ɹ��� 
		ConfigurationManager.getConfigInstance().setProperty("my-client.NFLoadBalancerRuleClassName", MyRule.class.getName());
		
		 RestClient client = (RestClient) ClientFactory.getNamedClient("my-client"); 
		 HttpRequest request = HttpRequest.newBuilder().uri(new URI("/hello/message/1")).build(); // 3
	      for (int i = 0; i < 20; i++)  {
	      	HttpResponse response = client.executeWithLoadBalancer(request); // 4
	      	String entity = response.getEntity(String.class);
	      	System.out.println(entity);
	      }
	}

}
