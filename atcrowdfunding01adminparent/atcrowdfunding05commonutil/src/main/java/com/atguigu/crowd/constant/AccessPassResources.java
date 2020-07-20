package com.atguigu.crowd.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname AccessPassResources
 * @Description TODO
 * @Date 2020/7/17 19:26
 */
public class AccessPassResources {
    public static final Set<String> PASS_RES_SET = new HashSet<String>();
    static {
        PASS_RES_SET.add("/");
        PASS_RES_SET.add("/auth/member/do/login");
        PASS_RES_SET.add("/auth/member/do/register");
        PASS_RES_SET.add("/auth/member/send/short/message.json");
        PASS_RES_SET.add("/auth/member/logout");
        PASS_RES_SET.add("/auth/member/to/login/page");
        PASS_RES_SET.add("/auth/member/to/reg/page");
    }
    public static final Set<String> STATIC_RES_SET = new HashSet<String>();
    static {
        STATIC_RES_SET.add("bootstrap");
        STATIC_RES_SET.add("css");
        STATIC_RES_SET.add("fonts");
        STATIC_RES_SET.add("img");
        STATIC_RES_SET.add("jquery");
        STATIC_RES_SET.add("layer");
        STATIC_RES_SET.add("script");
        STATIC_RES_SET.add("ztree");
    }
    public static boolean judgeCurrentServletPathWehterStaticResource(String servletPath){
        if (servletPath==null ||servletPath.length()==0){
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        String[] split = servletPath.split("/");
        String firstLevelPath = split[1];
        return STATIC_RES_SET.contains(firstLevelPath);
    }
}
