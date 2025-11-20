package com.taoge.framework.interceptor;

import com.taoge.framework.util.UUIDUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoggerMDCFilter extends OncePerRequestFilter implements Filter {
    protected final Logger log = LogManager.getLogger(getClass());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        ThreadContext.put("req.id", UUIDUtil.randomUUID().toUpperCase());
        try {
            chain.doFilter(request, response);
        } finally {
            ThreadContext.clearAll();
        }
    }
}
