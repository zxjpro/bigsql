# 数据源配置

bigsql不存储任何数据，只是一个路由转发并归的功能，所以真实的存储还是在物理数据库中，我们需要配置这些数据源

我们找到conf/datasource/目录，在这个目录中建立任意一个文件，文件名就是数据源的名称，名称不包含后缀名，比如有这样一个文件``abc.properties``，那么它对应的数据源名称就是``abc``

但是bigsql会扫描该目录的所有文件，所以我们需要保证该目录中的文件全部都是数据源的配置文件。

友情建议：我们可以使用一些比较友好的名字，可以让我们一眼就得知这是配置的哪个数据源，毕竟以后我们有可能要修改这些配置，为何不让自己好找一些呢？


## 配置格式

```properties
url=jdbc:mysql://192.168.31.233:3306/saas?useUnicode=true&characterEncoding=utf8&useSSL=false
username=root
password=123456
driverClassName=com.mysql.jdbc.Driver
#如果不配置，默认是100
maxActive=100
#如果不配置，默认是1
initialSize=1
#如果不配置，默认是60000
maxWait=60000
```

我们通常只需要配置这些属性就行了，其它的属性，bigsql会给一个较为合理的值。

但是``url``,``username``,``password``,``driverClassName``是必须配置的
