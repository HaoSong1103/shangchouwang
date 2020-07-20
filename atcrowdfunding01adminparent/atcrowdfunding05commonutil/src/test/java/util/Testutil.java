package util;

import com.atguigu.crowd.util.CrowdUtil;
import org.junit.Test;

/**
 * @author 宋浩
 * @version 1.0
 * @Classname Testutil
 * @Description TODO
 * @Date 2020/6/11 14:51
 */
public class Testutil {
    @Test
    public void testmd5(){
        String xx = "123456";
        System.out.println(CrowdUtil.md5(xx));
    }
}
