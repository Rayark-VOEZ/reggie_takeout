package com.reggie.takeout.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginCheckFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // chain.doFilter();

        // 1、获取本次请求的URI
        String requestURL = httpServletRequest.getRequestURL().toString();
        System.out.println(requestURL);
        chain.doFilter(request, response);

        // 2、判断本次请求是否需要处理
        // todo: 需要放行静态资源
        // if (requestURL.contains("login") ||
        //         requestURL.contains(".js") ||
        //         requestURL.contains(".css") ||
        //         requestURL.contains(".ico") ||
        //         requestURL.contains(".ttf") ||
        //         requestURL.contains(".woff") ||
        //         requestURL.contains(".png")) {
        //     // 3、如果不需要处理，则直接放行
        //     System.out.println("请求放行");
        //     chain.doFilter(request, response);
        //     return;
        // }

        // 4、判断登录状态，如果已登录，则直接放行
        // if (httpServletRequest.getSession().getAttribute("userId") != null) {
        //
        //     Long userId = (Long) ((HttpServletRequest) request).getSession().getAttribute("userId");
        //     BaseContext.setCurrentId(userId);
        //     System.out.println("请求放行");
        //     chain.doFilter(request, response);
        // }
        // 5、如果未登录则返回未登录结果
        // else {
        //     httpServletResponse.getWriter().write("未登录");
        // }
        // System.out.println("请求拦截");
    }
}
