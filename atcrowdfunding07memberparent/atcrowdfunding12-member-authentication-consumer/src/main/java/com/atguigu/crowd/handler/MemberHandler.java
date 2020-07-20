package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MySQLRemoteService;
import com.atguigu.crowd.api.RedisRemoteService;
import com.atguigu.crowd.constant.CrowdConstant;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.vo.MemberLoginVO;
import com.atguigu.crowd.entity.vo.MemberVO;
import com.atguigu.crowd.util.CrowdUtil;
import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname MemberHandler
 * @Description TODO
 * @Date 2020/7/15 21:58
 */
@Controller
public class MemberHandler {

    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendMessage(@RequestParam("phoneNum")String phoneNum){
        ResultEntity<String> postShortMessage = CrowdUtil.postShortMessage(phoneNum);
        //如果发送成功
        if (ResultEntity.SUCCESS.equals(postShortMessage.getResult())){
            String code = postShortMessage.getDate();
            ResultEntity<String> saveCode = redisRemoteService.setRedisKeyValueRemoteWithTimeout(
                    CrowdConstant.REDIS_CODE_PREFIX + phoneNum, code, 5, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(saveCode.getResult())){
                return ResultEntity.successWithoutData();
            }else {
                return saveCode;
            }
        }else {
            return postShortMessage;
        }
    }

    @RequestMapping("/auth/member/do/register")
    public String register(MemberVO memberVO,ModelMap modelMap){
        //1.获取用户手机号
        String phoneNum = memberVO.getPhoneNum();
        //2.拼Redis中存贮验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX+phoneNum;
        //3.从Redis读取key对应的value
        ResultEntity<String> resultEntity = redisRemoteService.getRedisStringValueByKeyRemote(key);
        //4.检查查询操作是否有效
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)){
            String redisCode = resultEntity.getDate();
            if (redisCode!=null){
                //5.如果从Redis能查询到value，则比较表单验证码和redis验证码
                String formCode = memberVO.getCode();
                if (Objects.equals(formCode, redisCode)){
                    //6.如果验证码一直，从redis删除
                    redisRemoteService.removeRedisKeyRemote(key);
                    //7.执行密码加密
                    String userpswd = memberVO.getUserpswd();
                    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                    String encode = passwordEncoder.encode(userpswd);
                    memberVO.setUserpswd(encode);
                    //8.执行保存
                    //1.创建一个空的MemberPO
                    MemberPO memberPO = new MemberPO();
                    //2.复制属性
                    BeanUtils.copyProperties(memberVO, memberPO);
                    //3.调用远程方法
                    ResultEntity<String> saveMember = mySQLRemoteService.saveMember(memberPO);
                    if (ResultEntity.FAILED.equals(saveMember.getResult())){
                        modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
                        return "member-reg";
                    }
                }else {
                    modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
                    return "member-reg";
                }
            }else {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
                return "member-reg";
            }
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }
        //使用重定向避免刷新浏览器导致重新执行注册流程
        return "redirect:/auth/member/to/login/page";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct")String loginacct,
            @RequestParam("userpswd")String userpswd,
            ModelMap modelMap,
            HttpSession session
    ){
        ResultEntity<MemberPO> resultEntity = mySQLRemoteService.getMemberPOByLoginAcctRemote(loginacct);
        if (ResultEntity.SUCCESS.equals(resultEntity.getResult())){
            MemberPO memberPO = resultEntity.getDate();
            if (memberPO==null){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
                return "member-login";
            }
            //比较密码
            String userpswd1 = memberPO.getUserpswd();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matchesResult = bCryptPasswordEncoder.matches(userpswd, userpswd1);
            if (!matchesResult){
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
                return "member-login";
            }
            MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getUsername(),
                    memberPO.getEmail(),memberPO.getId());
            session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
            return "redirect:/auth/member/to/center/page";
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }

    }

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
}
