package com.atguigu.crowd.test;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.mapper.MemberPOMapper;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * @author 宋浩
 * @version 1.0
 * @Classname MyBatisTest
 * @Description TODO
 * @Date 2020/7/14 16:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private MemberPOMapper memberPOMapper;

    private Logger logger = LoggerFactory.getLogger(MyBatisTest.class);

    @Test
    public void testMapper() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String source = "123123";
        String encode = passwordEncoder.encode(source);
        MemberPO memberPO = new MemberPO(null, "jack1", encode, "杰克",
                "jack@qq.com", 1, 1, "杰克", "123123", 2);
        memberPOMapper.insert(memberPO);
    }

    @Test
    public void testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        logger.debug(connection.toString());
    }

}
