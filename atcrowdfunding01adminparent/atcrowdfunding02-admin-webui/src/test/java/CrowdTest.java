import com.atguigu.crowd.entity.Admin;
import com.atguigu.crowd.entity.Role;
import com.atguigu.crowd.mapper.AdminMapper;
import com.atguigu.crowd.mapper.RoleMapper;
import com.atguigu.crowd.service.api.AdminService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname CrowdTest
 * @Description TODO
 * @Date 2020/6/9 16:35
 */

@RunWith(SpringJUnit4ClassRunner.class)
// 加载Spring 配置文件的注解
@ContextConfiguration(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleMapper roleMapper;

    @Test
    @Transactional
    public void testTx(){
        Admin admin = new Admin(null, "Tom", "123123", "汤姆", "tommao@qq.com", null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void testInsertAdmin(){
        Admin admin = new Admin(null, "tom", "123123", "xx", "tom@qq.com", null);
        int count = adminMapper.insert(admin);
        System.out.println(count);
    }

    @Test
    public void testInsertRole(){
        for (int i = 0; i < 100; i++) {
            roleMapper.insert(new Role(null,"role"+i));
        }
    }

    @Test
    public void addAdmin(){
        for (int i = 0; i <50 ; i++) {
            adminMapper.insert(new Admin(null, "jack"+i, "123123"+i, "xx"+i, "tom"+i+"@qq.com", null));
        }
    }

    @Test
    public void testDataSource() throws SQLException {
// 1.通过数据源对象获取数据源连接
        Connection connection = dataSource.getConnection();
// 2.打印数据库连接
        System.out.println(connection);
    }

    @Test
    public void testLog(){
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        logger.debug("123456789");
        logger.info("9999999");
        logger.warn("1111111");
        logger.error("0000000");
    }
}
