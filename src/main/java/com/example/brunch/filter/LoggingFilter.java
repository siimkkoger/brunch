package com.example.brunch.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * CustomFilter implements javax.Servlet.Filter.
 * This class has a @Component annotation to register as Spring bean in the application context.
 * This way, the DelegatingFilterProxy class can find our filter class while initializing the filter chain.
 */
@Component
public class LoggingFilter extends GenericFilterBean {

    private static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        LOGGER.info("Request Info : " + req);
        chain.doFilter(request, response);
    }
}