package com.reggie.takeout.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reggie.takeout.common.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 登录过滤器
 */
@Slf4j
@Component
public class LoginFilter implements Filter {

    private static final String[] EXCLUDE_PATHS = { // 排除过滤路径
            "/employee/login",
            "/employee/logout",
            "/backend/page/login/login.html",
            "/front/page/login.html",
            "/user/login",
            "/user/logout",
            "/user/sendMsg",
            "/**/api/**",
            "/**/images/**",
            "/**/js/**",
            "/**/plugins/**",
            "/**/styles/**",
            "/**/*.ico"
    };
    private AntPathMatcher pathMatcher = new AntPathMatcher(); // 路径匹配器

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // 1、获取本次请求的URI
        String requestURI = httpServletRequest.getRequestURI();
        log.info(requestURI);

        // 2、判断本次请求是否需要处理
        // 3、如果不需要处理，则直接放行
        for (String path: EXCLUDE_PATHS) {
            if (pathMatcher.match(path, requestURI)) {
                chain.doFilter(request, response);
                return;
            }
        }

        // 4、判断登录状态，如果已登录，则直接放行
        if (httpServletRequest.getSession().getAttribute("userId") != null) {
            Long userId = (Long) ((HttpServletRequest) request).getSession().getAttribute("userId");
            BaseContext.setCurrentId(userId);
            chain.doFilter(request, response);
            return;
        }

        // 5、如果未登录则返回未登录结果
        if (requestURI.contains("/front")) {
            httpServletResponse.sendRedirect("/front/page/login.html"); // 重定向
        }
        if (requestURI.contains("/backend")) {
            httpServletResponse.sendRedirect("/backend/page/login/login.html"); // 重定向
        }
    }
}
