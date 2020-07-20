package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.entity.RoleExample;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.RoleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname RoleServiceImpl
 * @Description TODO
 * @Date 2020/6/15 10:19
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public PageInfo<Role> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Role> roles = roleMapper.selectRoleByKeyword(keyword);
        return new PageInfo<>(roles);
    }

    @Override
    public void saveRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void updateRole(Role role) {
        roleMapper.updateByPrimaryKey(role);
    }

    @Override
    public void deleteRoles(List<Integer> roleList) {
        RoleExample roleExample = new RoleExample();
        RoleExample.Criteria criteria = roleExample.createCriteria();
        criteria.andIdIn(roleList);
        roleMapper.deleteByExample(roleExample);
    }

    @Override
    public List<Role> getAssignedRole(Integer adminId) {
        return roleMapper.selectAssignRole(adminId);
    }

    @Override
    public List<Role> getUnAssignedRole(Integer adminId) {
        return roleMapper.selectUnAssignRole(adminId);
    }
}
