# 未完成功能


## 监控EventLoopGroup的线程使用情况

使用Executor的构造方法即可

## 做一个WEB界面

另做一个程序，以socket连接

## 刷新配置

不停机刷新配置，数据源也可以刷新，但是只能增加数据源，如果修改数据源参数，比如修改最大连接数之类是不会刷新的，因为如果重新创建数据源，
那么需要关掉之前的数据源，会影响线上业务



## 批量插入
批量插入需要在insert代码中实现，是否考虑实现功能，因为事务的问题，有可能一部分成功，一部分失败

## navicat插入不显示自动增长主键 
在navicat中插入数据后，自动增长的主键不刷新，是因为没有返回客户端column信息，
