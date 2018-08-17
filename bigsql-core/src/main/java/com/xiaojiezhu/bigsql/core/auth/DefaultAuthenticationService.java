package com.xiaojiezhu.bigsql.core.auth;

import com.xiaojiezhu.bigsql.core.configuration.BigsqlConfiguration;
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

    private BigsqlConfiguration bigsqlConfiguration;

    public DefaultAuthenticationService(BigsqlConfiguration bigsqlConfiguration) {
        this.bigsqlConfiguration = bigsqlConfiguration;
    }

    @Override
    public boolean login(String username, byte[] password, byte[] random) {
        byte[] dbPassword = getDbPassword(username, random);
        boolean equals = Arrays.equals(dbPassword, password);
        return equals;
    }

    protected byte[] getDbPassword(String username,byte[] random){
        byte[] password = DegistUtil.sha1(bigsqlConfiguration.getPassword().getBytes());
        byte[] bytes = DegistUtil.xor(password,sha1(ByteUtil.concat(random,sha1(password)))) ;
        return bytes;
    }


}
