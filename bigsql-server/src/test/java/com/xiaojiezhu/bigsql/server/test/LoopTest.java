package com.xiaojiezhu.bigsql.server.test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.util.JdbcConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author xiaojie.zhu
 */
public class LoopTest {

    @Test
    public void test(){
        ByteBuf buffer = Unpooled.buffer();

        buffer.writeInt(Integer.MAX_VALUE);

        toString(buffer);


    }

    public void toString(ByteBuf buf){
        byte[] b = new byte[buf.readableBytes()];
        buf.readBytes(b);
        System.out.println(Arrays.toString(b));
    }

    @Test
    public void test2() throws InterruptedException {
        String s = "show create TABLE `user`;";
        s = s.replaceAll("`","");
        System.out.println(s);

    }

}
