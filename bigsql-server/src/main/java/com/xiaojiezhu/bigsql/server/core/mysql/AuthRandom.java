package com.xiaojiezhu.bigsql.server.core.mysql;

import com.xiaojiezhu.bigsql.util.ByteUtil;

import java.util.Random;

/**
 * @author xiaojie.zhu
 */
public class AuthRandom {

    private byte[] r1;
    private byte[] r2;
    /**
     * total of r1 and r2
     */
    private byte[] r3;



    public AuthRandom() {
        r1 = randomBytes(8);
        r2 = randomBytes(12);
        r3 = ByteUtil.concat(r1,r2);
    }


    public byte[] getR1() {
        return r1;
    }

    public void setR1(byte[] r1) {
        this.r1 = r1;
    }

    public byte[] getR2() {
        return r2;
    }

    public void setR2(byte[] r2) {
        this.r2 = r2;
    }

    public byte[] getR3() {
        return r3;
    }

    public void setR3(byte[] r3) {
        this.r3 = r3;
    }

    public static byte[] randomBytes(int len){
        Random r = new Random();
        byte[] buf = new byte[len];
        for(int i = 0 ; i < len; i++){
            //the random byte can not be 0 , because it will be login fail.
            buf[i] = (byte) (1 + r.nextInt(126));
        }
        return buf;
    }


}
