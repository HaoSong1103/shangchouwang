package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Auth;

import java.util.List;
import java.util.Map;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname AuthService
 * @Description TODO
 * @Date 2020/6/17 19:30
 */
public interface AuthService {
    List<Auth> getAll();

    List<Auth> getAssignedAuthIdByRoleId(Integer roleId);

    void saveRoleAuthRelationship(Map<String, List<Integer>> map);

    List<String> getAssignedAuthNameByAdminId(Integer adminId);
}
