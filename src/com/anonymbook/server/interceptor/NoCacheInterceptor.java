package com.anonymbook.server.interceptor;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;

@Intercepts
@RequestScoped
public class NoCacheInterceptor implements Interceptor {

	private final HttpServletResponse response;

	public NoCacheInterceptor(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public boolean accepts(ResourceMethod method) {
		return true;
	}

	@Override
	public void intercept(InterceptorStack stack, ResourceMethod method, Object resourceInstance) throws InterceptionException {
		
		response.setHeader("Expires", "Wed, 01 Jan 1977 10:00:00 GMT");

		// no-cache HTTP/1.0
		response.setHeader("Pragma", "no-cache");

		// no-cache HTTP/1.1
		response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

		// no-cache HTTP/1.1 (IE)
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");

		stack.next(method, resourceInstance);
	}
}