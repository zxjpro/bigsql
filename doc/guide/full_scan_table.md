# 引发全表扫描

:::warning 警告
在有很多分表，并且数据量很大的情况下，进行全表扫描几乎是一场灾难，如果要查询样例数据，我们何不加上分片键并且``limit 1``呢？
:::

如果不能合理的应用分片键，那么将会引发全表扫描，其实不仅仅是``SELECT``语句，像``UPDATE``语句以及``DELETE``都会引发全表操作，只不过``SELECT``的危害最大

这里说明一下怎么样的SQL会进行全表扫描，我们假定``name``属性为分片键。

## 没有分片键

最简单的，当我们没有使用分片键时，bigsql会引发全表扫描，因为没有分片键，bigsql就没有办法定位数据分片的位置，只能进行全表扫描。

## 不等于分片键
另一方面，即使我们的语句中已经有了分片键中的值，但是如果进行的是不等于操作，还是引会全表扫描，SQL如下:

```sql
SELECT * FROM `user` WHERE `name` !='abc';
SELECT * FROM `user` WHERE `name` NOT IN ('abc');

DELETE FROM `user` WHERE `name` != 'abc';

UPDATE `user` set age=10 WHERE `name` != 'abc';

```

## 带有 OR 的语句

我们知道，``OR``是或者的意思，所以我们即使是在语句中带有了分片键，我们也无法定位到一个准确的数据源，bigsql只能进行全表扫描，语句如下：

```sql
SELECT * FROM `user` WHERE `name` = 'abc' or age = 28;

DELETE FROM `user` WHERE `name` = 'abc' or age = 28;

UPDATE `user` set age=10 WHERE `name` = 'abc' or age = 28;

```

如果我们要查询分片键中的多种可能，我们其实可以把它换成``IN``,如下:

```sql
SELECT * FROM  `user` WHEN `name` = 'abc' or `name` ='xxx';
```

我们可以换成这种写法，这是不会引发全表扫描的
```sql
SELECT * FROM  `user` WHEN `name` IN ('abc','XXX');
```

## 禁用全表扫描

因为全表扫描是代价是非常大的，在分布式数据库中，我们一般不会写出这样的语句，但是也无法完全避免开发人员的误操作，所以bigsql提供了一个参数来禁止执行全表扫描的语句，一旦遇到这样的语句，就会返回ERROR。
设置方式如下，我们进入表规则的配置，在``properties``中加上下面的内容

```xml
<property>
    <key>allowFullScan</key>
    <value>true</value>
</property>
```

如果你还不清楚，那么可以参考建立逻辑表的配置方式