package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.Menu;

import java.util.List;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname MenuService
 * @Description TODO
 * @Date 2020/6/16 10:24
 */
public interface MenuService {
    public List<Menu> getAll();

    void save(Menu menu);

    void update(Menu menu);

    void remove(Integer id);
}
