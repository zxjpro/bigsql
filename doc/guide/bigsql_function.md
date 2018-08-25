# bigsql的其它功能

## 显示客户端到bigsql的连接

在mysql数据库中，我们可以查看所有客户到与mysql服务端的连接情况，``SHOW PROCESSLIST``命令可以查看所有的连接情况。

同样的，bigsql也支持这个命令，bigsql作为mysql的代理服务器，同样也需要查看bigsql有哪些连接，所以bigsql也支持``SHOW PROCESSLIST``语句。

而且bigsql中查询的结果与mysql中的查询结果格式是一样的。

:::tip 小提示
在bigsql的元数据库中，有一个``CONNECTION``表，这个表记录的就是连接列表，查询这个表的内容，和``SHOW PROCESSLIST``或者``SHOW FULL PROCESSLIST``的结果是一样的。
:::