package com.xiaojiezhu.bigsql.test.stretegycreate;

import com.xiaojiezhu.bigsql.sharding.sharding.hash.SingleColumnHashShardingStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * @author xiaojie.zhu
 */
public class CreateInstanceTest {

    @Test
    public void test(){
        Class<SingleColumnHashShardingStrategy> aClass = SingleColumnHashShardingStrategy.class;

        Constructor<?>[] constructors = aClass.getConstructors();
        Constructor<?> constructor = constructors[0];

        Class<?>[] parameterTypes = constructor.getParameterTypes();

        Assert.assertEquals(3, parameterTypes.length);
    }
}
