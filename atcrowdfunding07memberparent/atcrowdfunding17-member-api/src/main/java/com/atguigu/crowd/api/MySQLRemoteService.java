package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * @author 宋浩
 * @version 1.0
 * @Classname MySQLRemoteService
 * @Description TODO
 * @Date 2020/7/14 19:58
 */
@FeignClient("atguigu-crowd-mysql")
public interface MySQLRemoteService {

    @RequestMapping("/get/memverpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote
            (@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO);
}
