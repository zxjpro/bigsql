# 事务

bigsql实现了对于分库分表中的分布式事务，这是一个集中式事务管理，无论有多少个分库，多少个分表，都可以和往常一样使用事务。


## 使用事务

使用事务的方式并没有任何改变，我们和往常一样使用就可以了，任何语言任何客户端都可以使用。以java为例：

如果在spring中配置了事务管理器，那么我们使用``@Transaction``注解就可以了。

如果是在jdbc中

```java
Connection conn = ...;
conn.setAutoCommit(false);

//提交
conn.commit();
//或者回滚
conn.rollback();
```




