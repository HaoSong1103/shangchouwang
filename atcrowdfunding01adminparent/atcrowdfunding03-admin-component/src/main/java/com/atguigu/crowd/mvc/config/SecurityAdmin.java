package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname SecurityAdmin
 * @Description 考虑到User 对象中仅仅包含账号和密码，为了能够获取到原始的Admin 对象，专门创建这个类对User 类进行扩展
 * @Date 2020/6/23 16:03
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;

    private Admin originalAdmin;

    /**
     *
     * @param originalAdmin 传入原始的Admin对象
     * @param authorities 创建角色、权限信息的集合
     */
    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities) {
        //调用父类构造器
        super(originalAdmin.getLoginAcct(),originalAdmin.getUserPswd(), authorities);
        //将Admin传给类的属性
        this.originalAdmin = originalAdmin;
        //擦除密码
        this.originalAdmin.setUserPswd(null);
    }

    //对外提供获取原始Admin对象的get方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
