package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname RoleService
 * @Description TODO
 * @Date 2020/6/15 10:19
 */
public interface RoleService {
    PageInfo<Role> getPageInfo(String keyword , Integer pageNum , Integer pageSize);

    void saveRole(Role role);

    void updateRole(Role role);

    void deleteRoles(List<Integer> roleList);

    List<Role> getAssignedRole(Integer adminId);

    List<Role> getUnAssignedRole(Integer adminId);
}
