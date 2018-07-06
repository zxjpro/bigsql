---
home: true
heroImage: /hero.png
actionText: 快速开始 →
actionLink: /guide/
features:
- title: 分库分表
  details: 解决在大数据时代，海量数据在关系型数据库中的存储问题，将一个大表的数据，分布在不同的数据库实例，不同的表中，如果你不想分库，那么也可以仅仅分表。
- title: 读写分离
  details: 优化读写性能，将读取数据库的操作，与写入数据库的操作分离开，提升数据库的性能。
- title: 极简配置
  details: Bigsql提供的参数极其简单，我们可以在一分钟内看懂简单的分库分表，并且实现自己想要的功能。
- title: 插件
  details: Bigsql提供了插件机制，使得我们在不需要重新打包源码，甚至不需要重启Bigsql服务的情况下，增加我们自定义的插件，比如说SQL拦截，自定义分库分表插件等。
- title: 事务
  details: Bigsql计划提供一个与单机数据库体验一样的事务，从而使得开发人员和应用程序不必关心分布式事务的问题，开发人员只需要和往常一样使用事务就行了，而分布式的事务，我们交给Bigsql就好。
- title: 监控
  details: Bigsql能及时的反馈出当前的运行情况，比如前当的负载情况，以及对于一些SQL会提出相应的优化建议，对于慢SQL会记录下来，同时也提供优化建议。
footer: apache Licensed 2 | Copyright © 2018-present 朱小杰
---

# 简介

其实我们可以把bigsql看成是一台mysql-server服务器，因为bigsql实现了mysql-server的协议，我们可以使用jdbc,odbc，以及navicat来连接它，和我们平时使用mysql数据库是一样的。

所以无论是java,.net,python,php,c/c++,nodejs等，都可以在不修改任何代码的情况下使用它，无论是否使用了任何框架

![](http://wx4.sinaimg.cn/mw690/005ZQTvlgy1ft0iupn8kaj30rw0jb3zp.jpg)

