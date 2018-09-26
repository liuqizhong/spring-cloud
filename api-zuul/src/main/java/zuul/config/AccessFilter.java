package zuul.config;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * 自定义过滤器
 * @author 67zhong
 *
 */
@Component
public class AccessFilter extends ZuulFilter{
	
	private Logger logger = LoggerFactory.getLogger(AccessFilter.class);

	@Override
	public Object run() {
		RequestContext currentContext = RequestContext.getCurrentContext();
		HttpServletRequest request = currentContext.getRequest();
		
		logger.info("send {} request to {}", request.getMethod(), 
				request.getRequestURL().toString());
		
		String token = request.getParameter("token");
		if(token == null) {
			currentContext.setSendZuulResponse(false);
			currentContext.setResponseStatusCode(401);
			return null;
		}
			
		System.out.println("---自定义过滤器---");
		return null;
	}

	/**
	 * 是否执行过滤器(是否执行自己)
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 执行的优先级，越小越先执行
	 */
	@Override
	public int filterOrder() {
		return 0;
	}

	/**
	 * 过滤器类型，有三种，pre(请求路由前执行), routing(请求路由转发到目标服务实例，请求+响应), post(可以获取目标实例返回的结果，最终响应请求)
	 */
	@Override
	public String filterType() {
		return FilterConstants.ROUTE_TYPE;
	}

}
