package com.atguigu.crowd.filter;

import com.atguigu.crowd.constant.AccessPassResources;
import com.atguigu.crowd.constant.CrowdConstant;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname CrowdAccessFilter
 * @Description TODO
 * @Date 2020/7/17 19:47
 */
@Component
public class CrowdAccessFilter extends ZuulFilter {
    @Override
    public String filterType() {
        // 这里返回“pre”意思是在目标微服务前执行过滤
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        //返回false代表方向，返回true表示不能放行
        //1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2.通过RequestContext对象获取当前请求（框架地城是借助ThreadLocal从当前线程上获取实现绑定的对象）
        HttpServletRequest request = requestContext.getRequest();
        //3.获取servletPath
        String servletPath = request.getServletPath();
        //4.根据servletPath判断是否是可以直接放行的请求
        boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);
        if (containsResult) return false;
        return !AccessPassResources.judgeCurrentServletPathWehterStaticResource(servletPath);
    }

    @Override
    public Object run() throws ZuulException {
        //1.获取RequestContext对象
        RequestContext requestContext = RequestContext.getCurrentContext();
        //2.通过RequestContext对象获取当前请求（框架地城是借助ThreadLocal从当前线程上获取实现绑定的对象）
        HttpServletRequest request = requestContext.getRequest();
        //3.获取当前Session对象
        HttpSession session = request.getSession();
        //4.尝试从Session对象中获取已登录的用户
        Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        //5.判断loginMember是否为空
        if (loginMember==null){
            //5.获取response对象
            HttpServletResponse response = requestContext.getResponse();
            //6.将消息存入Session域
            session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);
            //7.重定向到auth-consumer工程的登录页面
            try {
                response.sendRedirect("/auth/member/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
