package com.atguigu.crowd.util;


import com.atguigu.crowd.constant.CrowdConstant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname CrowdUtil
 * @Description 尚筹网项目通用方法类
 * @Date 2020/6/10 17:36
 */
public class CrowdUtil {

    /**
     * 判断当前请求是否为Ajax请求
     * @param request 请求对象
     * @return
     * 		true：当前请求是Ajax请求
     * 		false：当前请求不是Ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request) {
        // 1.获取请求消息头信息
        String acceptInformation = request.getHeader("Accept");
        String xRequestInformation = request.getHeader("X-Requested-With");
        // 2.检查并返回
        return (acceptInformation != null && acceptInformation.length() > 0 && acceptInformation.contains("application/json")) ||
                (xRequestInformation != null && xRequestInformation.length() > 0 && xRequestInformation.equals("XMLHttpRequest"));
    }

    public static String md5(String source){
        //1.判断source是否有效
        if (source==null || source.length()==0){

            //2.如果不是有效的字符串抛出异常
            throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
        }
        try {
            //3.获取MessageDigest对象
            String algorithm = "md5";
            MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

            //4.获取明文字符串对应的字节数组
            byte[] input = source.getBytes();

            //5.执行加密
            byte[] output = messageDigest.digest(input);

            //6.创建一个BigInteger对象
            int signum = 1;
            BigInteger bigInteger = new BigInteger(signum, output);

            //7.按照16进制将BigInteger的值转化为字符串
            int radix = 16;
            String encoded = bigInteger.toString(radix);

            return encoded;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    //模拟短信发送,验证码默认为电话号前4位
    public static ResultEntity<String> postShortMessage(String phoneNum){
        String code = phoneNum.substring(0,4);
        return ResultEntity.successWithData(code);
    }

}
