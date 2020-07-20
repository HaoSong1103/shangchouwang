package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname AdminService
 * @Description TODO
 * @Date 2020/6/9 19:27
 */
public interface AdminService {
    void saveAdmin(Admin admin);

    List<Admin> getAll();

    Admin getAdminByLoginAcct(String acct, String loginAcct);

    PageInfo<Admin> getPageInfo(String keyword ,Integer pageNum ,Integer pageSize);

    void remove(Integer adminId);

    Admin getAdminById(Integer adminId);

    void update(Admin admin);

    void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList);

    Admin getAdminByLoginAcct(String username);
}
