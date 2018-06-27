package com.xiaojiezhu.bigsql.core.auth;

import com.xiaojiezhu.bigsql.util.ByteUtil;
import com.xiaojiezhu.bigsql.util.codec.DegistUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.xiaojiezhu.bigsql.util.codec.DegistUtil.sha1;

/**
 * @author xiaojie.zhu
 */
public class DefaultAuthenticationService implements AuthenticationService {
    public final static Logger LOG = LoggerFactory.getLogger(DefaultAuthenticationService.class);
    private String username = "root";
    private byte[] password = DegistUtil.sha1("123".getBytes());

    @Override
    public boolean login(String username, byte[] password, byte[] random) {
        byte[] dbPassword = getDbPassword(username, random);
        boolean equals = Arrays.equals(dbPassword, password);
        if(!equals){
           // LOG.warn("password not equals , " + Arrays.toString(dbPassword) + " : " + Arrays.toString(password));
        }
        return equals;
    }

    protected byte[] getDbPassword(String username,byte[] random){
        byte[] bytes = DegistUtil.xor(this.password,sha1(ByteUtil.concat(random,sha1(this.password)))) ;
        return bytes;
    }


}
