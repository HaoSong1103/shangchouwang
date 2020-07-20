package com.atguigu.crowd.handler;

import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.service.api.MemberService;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname MemberProviderHandler
 * @Description TODO
 * @Date 2020/7/14 20:03
 */

@RestController
public class MemberProviderHandler {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memverpo/by/login/acct/remote")
    public ResultEntity<MemberPO> getMemberPOByLoginAcctRemote
            (@RequestParam("loginacct") String loginacct){
        try {
            //调用本地Service查询
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
            //如果没有抛异常，返回成功结果
            return ResultEntity.successWithData(memberPO);
        } catch (Exception e) {
            e.printStackTrace();

            //捕获到异常，返回失败的结果
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/member/remote")
    public ResultEntity<String> saveMember(@RequestBody MemberPO memberPO){
        try {
            memberService.saveMember(memberPO);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            if (e instanceof DuplicateKeyException){
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }
}
