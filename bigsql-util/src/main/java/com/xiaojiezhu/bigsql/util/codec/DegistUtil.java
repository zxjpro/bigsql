package com.xiaojiezhu.bigsql.util.codec;

import sun.rmi.runtime.RuntimeUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaojie.zhu
 */
public class DegistUtil {

    public static byte[] sha1(byte[] b){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
            byte[] digest = md.digest(b);
            return digest;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] xor(byte[] a,byte[] b){
        if(a == null || b == null){
            throw new NullPointerException("byte array can not be null");
        }
        if(a.length != b.length){
            throw new RuntimeException("byte array length must be equals");
        }
        byte[] buf = new byte[a.length];
        for(int i = 0 ; i < a.length;i++){
            int i1 = a[i] ^ b[i];
            buf[i] = (byte) i1;
        }
        return buf;
    }

}
