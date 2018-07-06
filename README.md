# bigsql

#### 项目介绍
mysql分库分表中间件，实现了mysql-server协议，任何语言及客户端都可以使用，包括mysql图形化工具，navicat等，配置简单。

**[DOCUMENT](http://bigsql.xiaojiezhu.com "document")**

# 系统架构

![](http://wx4.sinaimg.cn/mw690/005ZQTvlgy1ft0iupn8kaj30rw0jb3zp.jpg)

bigsql是把自己伪装成了一个mysql的服务器，所以任何语言，任何框架都可以很轻松的使用它，并且不需要修改任何代码，就可以拥有读写海量数据的能力。