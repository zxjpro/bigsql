# 小提示

## 尽可能的少用join
在分布式关系型数据库中，我们需要尽可能的少用join，因为本身join字段出现在分布式关系型数据库中就是不合理的存在，在分库分表中，我们无法利用到数据库自带的索引来进行join，
只能依靠中间件自己实现，但是性能却非常低，甚至很多公司在分布式关系型数据库中都是禁用join的。

解决方案：
- 合理的设计表结构，当我们考虑某一个表做分片时，我们就该想清楚，这个表该做什么，不该做什么，适用的业务场景是什么。如果涉及报表之类的查询，为什么不用别的方案呢？spark,hadoop，或者一些nosql数据库？
- 合理的对一些数据进行冗余。这需要我们对目前的数据库非常了解，并且知道当前的业务情况，并且对未来发生的变化有一些规划，才能合理的对数据表进行冗余，这是分布式数据库中常用的方案。

## 数据源配置

conf/datasource/目录中是配置物理数据源的文件，所以我们可能对它的安全性要求很高，所以我们可以用一个特殊的用户启动bigsql,
然后conf/datasource/只对这个用户读取写权限，其它的用户则就没有读写权限，则可以保证数据源配置参数的安全

总结起来就是设置该文件的读取权限，让其它用户没有读取权限就行了嘛

## 编码
无论是bigsql,还是后端mysql实例，或者是应用程序，为了避免问题，请尽量使用utf8mb4,utf8,utf8_general_ci


## 刷新数据源配置

bigsql提供了在不停机的情况下，增加数据源配置的功能，但是值得注意的是，bigsql并不会修改任何已经加载过的数据源，因为该数据源有可能正在使用，
bigsql只会对新增加的数据源进行增加，所以请不要修改之前的数据源，特别是修改数据源的name，这会引发无法加载正确的数据源的问题。


## 使用mysql命令行

当我们使用mysql命令行客户端时
```sh
mysql -P 3307 -h 10.8.1.69 -u root -p
use database;
```

在执行切换数据库时，会卡住，出现如下信息，并且永远卡死在这里

```
mysql> use sharding;
Reading table information for completion of table and column names
You can turn off this feature to get a quicker startup with -A
```

其实按照这里给出来的提示，我们需要在启动mysql客户端时加上-A参数就行了，如下

```sh
mysql -P 3307 -h 10.8.1.69 -u root -p -A
```

加上``-A``就代表着不会预读数据库信息，就可以正常执行后面的操作


## SQL查询顺序

我们编写SQL时，需要把带有分片的表名写在前面，bigsql会加载第一个表的分片策略。

假如我们对table_b进行了分片，我们要对table_a进行join

```sql
SELECT * FROM table_b b inner join table_a on a.id=b.id
```

也就是说，我们要以分片表为主表进行join。

## 分片键
- 如果一个语句不带有分片键，那么将会引发全表扫描。
- 如果``INSERT``语句中不带有分片键，那么将会返回ERROR，无法执行。


## 命名限制
- 一个表中的字段名，不能与表名一致。
- 不要使用MYSQL中的关键字在数据库中命名。
- 不要使用bigsql中的关键字在数据库中命名，[bigsql关键字](/guide/keyword.md)

## INSERT语句写法

insert语句中，必须写明要插入哪些字段，如下

```sql
INSERT INTO tableName(id,name,age)value(1,'name',20);
```

而不能写成

```sql
INSERT INTO tableName value(1,'name',20);
```