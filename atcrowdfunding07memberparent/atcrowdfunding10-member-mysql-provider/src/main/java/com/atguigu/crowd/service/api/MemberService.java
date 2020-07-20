package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.po.MemberPO;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname MemberService
 * @Description TODO
 * @Date 2020/7/14 20:06
 */
public interface MemberService {
    public MemberPO getMemberPOByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);
}
