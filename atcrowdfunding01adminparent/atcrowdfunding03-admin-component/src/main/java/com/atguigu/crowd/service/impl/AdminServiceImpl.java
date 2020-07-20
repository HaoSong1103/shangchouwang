package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.AdminExample;
import com.atguigu.crowd.exception.LoginAcctAlreadyInUseException;
import com.atguigu.crowd.exception.LoginFailedException;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.service.api.AdminService;
import com.atguigu.crowd.util.CrowdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname AdminServiceImpl
 * @Description TODO
 * @Date 2020/6/9 19:27
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveAdmin(Admin admin) {
        //密码加密
        String userPswd = admin.getUserPswd();
//        userPswd = CrowdUtil.md5(userPswd);
        userPswd =passwordEncoder.encode(userPswd);
        admin.setUserPswd(userPswd);
        //生成创建时间
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String createTime = format.format(date);
        admin.setCreateTime(createTime);
        //执行保存

        try {
            adminMapper.insert(admin);
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof DuplicateKeyException){
                throw new LoginAcctAlreadyInUseException(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
        }
    }

    @Override
    public List<Admin> getAll() {
        return adminMapper.selectByExample(new AdminExample());
    }

    @Override
    public Admin getAdminByLoginAcct(String loginName, String loginPswd) {
        //1.根据登录账户查询admin对象
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(loginName);
        List<Admin> list = adminMapper.selectByExample(adminExample);
        //2.判断Admin对象是否为null
        if (list ==null || list.size()==0) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        if (list.size()>1){
            throw new RuntimeException(CrowdConstant.MESSAGE_SYSTEM_ERROR_LOGIN_NOT_UNIQUE);
        }
        Admin admin = list.get(0);
        //3.如果admin对象是null，抛出异常
        if (admin==null) {
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //4.如果admin对象不是null，将数据库密码从admin对象中取出
        String userPswdDB = admin.getUserPswd();
        //5.将表单提交的明文密码进行加密
        String userPswdForm = CrowdUtil.md5(loginPswd);
        //6.对密码进行比较
        if (!Objects.equals(userPswdDB, userPswdForm)){
            //7.如果密码不一致则抛出异常
            throw new LoginFailedException(CrowdConstant.MESSAGE_LOGIN_FAILED);
        }
        //8.如果一致返回admin对象
        return admin;
    }

    @Override
    public PageInfo<Admin> getPageInfo(String keyword, Integer pageNum, Integer pageSize) {
        //1.调用PageHelper的静态方法开启分页功能
        PageHelper.startPage(pageNum, pageSize);

        //2.执行查询
        List<Admin> adminList = adminMapper.selectAdminByKeyword(keyword);

        //3.封装到PageInfo对象中
        return new PageInfo<>(adminList);
    }

    @Override
    public void remove(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void update(Admin admin) {
        adminMapper.updateByPrimaryKeySelective(admin);
    }

    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        //1.删除旧关联
        adminMapper.deleteOLdRelationship(adminId);
        //2.保存新关联
        if (roleIdList !=null && roleIdList.size() > 0)
        adminMapper.insertNewRelationship(adminId,roleIdList);
    }

    @Override
    public Admin getAdminByLoginAcct(String username) {
        AdminExample adminExample = new AdminExample();
        AdminExample.Criteria criteria = adminExample.createCriteria();
        criteria.andLoginAcctEqualTo(username);
        return adminMapper.selectByExample(adminExample).get(0);
    }

    @Override
    public Admin getAdminById(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }
}
