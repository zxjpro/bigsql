# 运行参数
bigsql需要读取一些外部的配置文件，才能完整的的运行

- bigsql的一些环境变量
- bigsql的物理数据源配置
- bigsql的数据库，表配置
- bigsql的表路由规则

如上的配置，全部会在conf文件夹中，所以我们运行的时候，只需要指定conf文件夹的位置就行了，但是conf文件夹中的目录不能移动，需要保持原来的位置，
bigsql会根据相对路径去查找

```sh
java -Dbigsql.conf=/xxPath/conf/ -jar bigsql.jar
```