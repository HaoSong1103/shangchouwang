package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.service.api.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname TestHandler
 * @Description TODO
 * @Date 2020/6/10 10:18
 */
@Controller
public class TestHandler {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap){
        List<Admin> adminList = adminService.getAll();
//        System.out.println("已进入");
        modelMap.addAttribute("adminList", adminList);

        return "target";
    }

    @ResponseBody
    @RequestMapping("/send/array/one.json")
    public String testReceiveArrayOne(@RequestParam("array[]")List<Integer> array){
        for(Integer num:array){
            System.out.println("number"+num);
        }
        return  "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/two.json")
    public String receiveArrayPlanThree(@RequestBody List<Integer> array) {
        for(Integer num:array){
            System.out.println("number"+num);
        }
        return  "success";
    }
}
