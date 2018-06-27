package com.xiaojiezhu.bigsql.core.auth;

/**
 * @author xiaojie.zhu
 */
public interface AuthenticationService {

    boolean login(String username,byte[] password,byte[] random);
}
