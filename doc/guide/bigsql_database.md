# bigsql元数据库

bigsql在启动时，会自带有一个名为``bigsql``的数据库，这个是bigsql的**元数据库**，存储有bigsql实例中的一些信息



## CONNECTION(连接信息)

> 这个表描述了客户端连接bigsql的信息，实际上它的效果等于``SHOW FULL PROCESSLIST``。

|Id|User|Host|db|Command|Time|State|Info|
|-|-|-|-|-|-|-|-|
|6|root|0:0:0:0:0:0:0:1|bigsql|null|0|null|SELECT * FROM `CONNECTION` LIMIT 0, 1000|
|5|root|0:0:0:0:0:0:0:1|sharding|null|1102|null|SHOW CREATE TABLE `person`|
|2|root|0:0:0:0:0:0:0:1|bigsql|null|1117|null|SHOW CREATE TABLE `CONNECTION`|

- ``Id`` 每个客户端连接bigsql时，会分配一个id
- ``User`` 当前连接登陆的用户名
- ``Host`` 客户端的ip
- ``db`` 当前连接的数据库名
- ``Command`` 这个为null，只是为了保持与mysql返回格式的一致
- ``Time`` 显示这个客户端连接多久了，单位是秒
- ``State`` 这个为null，只是为了保持与mysql返回格式的一致
- ``Info`` 这个是本连接当前正在执行的SQL



## CONNECTION_POOL(连接池与真实库的连接)

> bigsql对接着多个mysql实例，它对每个mysql实例都维持着一个连接池，所以``CONNECTION_POOL``表中，描述的是当前连接池的信息。

|dataSourceName|activeCount|activePeak|createCount|maxActive|
|-|-|-|-|-|
|db0 |0|3|3|100|

- ``dataSourceName`` 数据源的名称，每一个数据源名称都对应着一个连接池，这是在配置文件中指定的。
- ``activeCount`` 当前使用连接池中连接的数量。
- ``activePeak`` 在启动以后，使用连接池的峰值，也就是最大值。
- ``createCount`` 当前连接池与Mysql实例建立的连接数量。
- ``maxActive`` 连接池中配置的允许建立的最大连接。