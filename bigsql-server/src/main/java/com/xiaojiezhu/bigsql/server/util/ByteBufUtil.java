package com.xiaojiezhu.bigsql.server.util;

import com.xiaojiezhu.bigsql.model.constant.Constant;
import com.xiaojiezhu.bigsql.common.exception.BigSqlException;
import io.netty.buffer.ByteBuf;

import java.util.Arrays;

/**
 * @author xiaojie.zhu
 */
public class ByteBufUtil {

    public static String toString(ByteBuf byteBuf){
        int len = byteBuf.readableBytes();
        byte[] buf = new byte[len];
        byteBuf.readBytes(buf);
        return Arrays.toString(buf);
    }

    public static int lengthEncodedIntegerLength(long value){
        if(value <= Constant._250){
            return 1;
        }else if(value >= Constant._251 && value < Math.pow(Constant._2,Constant._16)){
            return 3;
        }else if(value >= Math.pow(Constant._2,Constant._16) && value < Math.pow(Constant._2,Constant._24)){
            return 4;
        }else if(value >= Math.pow(Constant._2,Constant._24) && value < Math.pow(Constant._2,Constant._64)){
            return 9;
        }else{
            throw new BigSqlException("error length code binary :" + value);
        }
    }
}
