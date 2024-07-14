package id.go.bpjskesehatan.service.v2;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CORSFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest) servletRequest;
	
	    HttpServletResponse resp = (HttpServletResponse) servletResponse;
	    resp.addHeader("Access-Control-Allow-Origin","*");
	    resp.addHeader("Access-Control-Allow-Methods","GET, POST, PUT, DELETE, OPTIONS, HEAD");
	    resp.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept, Authorization");
	
	    if ( request.getMethod().equals("OPTIONS") ) {
	        resp.setStatus(HttpServletResponse.SC_OK);
	        return;
	    }
	    chain.doFilter(request, servletResponse);
    }

    @Override
    public void destroy() {}
}