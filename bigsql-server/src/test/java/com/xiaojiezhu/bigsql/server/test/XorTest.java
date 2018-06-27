package com.xiaojiezhu.bigsql.server.test;

import com.xiaojiezhu.bigsql.sql.resolve.statement.ShowComponentStatement;
import org.junit.Test;

/**
 * @author xiaojie.zhu
 */
public class XorTest {


    @Test
    public void test(){
        System.out.println(0xfa);
        System.out.println(0xfb);
        System.out.println(0xfc);
        System.out.println(0xfd);
        System.out.println(0xfe);
    }

    @Test
    public  void test2() {
        ShowComponentStatement s = new ShowComponentStatement("SHOW DATABASES");

        System.out.println(s.getShowType());
    }

}
