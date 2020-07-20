package com.atguigu.crowd.mvc.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.service.impl.AdminServiceImpl;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname AdminHandler
 * @Description TODO
 * @Date 2020/6/11 10:48
 */
@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    //进入用户管理界面
    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            ModelMap modelMap
    ) {
        //调用service方法获取PageInfo
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword, pageNum, pageSize);
        //将PageInfo放入到modelMap中
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO, pageInfo);
        return "admin-page";
    }

    //用户登录
    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct, @RequestParam("userPswd") String userPswd, HttpSession session){
        //调用Service方法执行登录检查，如果返回Admin代表登录成功,失败则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);
        //登录成功后将返回的Admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        return "redirect:/admin/to/main/page.html";
    }

    //用户登出
    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        session.invalidate();
        return "redirect:/admin/to/login/page.html";
    }

    //根据用户删除用户数据
    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword
    ){
        //执行删除
        adminService.remove(adminId);

        //页面跳转
        //使用重定向
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @RequestMapping("/admin/save.html")
    public String saveAdmin(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(
            @RequestParam("adminId") Integer adminId,
            ModelMap modelMap
    ){
        //1.查询Admin对象
        Admin admin = adminService.getAdminById(adminId);
        //2.将admin对象存入模型
        modelMap.addAttribute("admin", admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String update(@RequestParam("pageNum") Integer pageNum,
                         @RequestParam("keyword") String keyword,Admin admin){
        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }
}
