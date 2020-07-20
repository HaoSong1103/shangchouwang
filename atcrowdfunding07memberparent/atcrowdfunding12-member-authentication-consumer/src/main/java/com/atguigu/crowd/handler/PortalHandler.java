package com.atguigu.crowd.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname PortalHandler
 * @Description TODO
 * @Date 2020/7/14 22:53
 */
@Controller
public class PortalHandler {
    @RequestMapping("/")
    public String showPortalPage() {
        // 这里实际开发中需要加载数据……
        return "portal";
    }
}
