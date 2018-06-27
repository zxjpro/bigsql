# 提示

## 启动参数
在开发环境中启动bigsql,需要配置下面的启动参数

- ``-Dbigsql.dev=true``

代表这是开发环境，否则会报错

- ``-Dbigsql.conf=E:\code\work_space\bigsql\bigsql-server\conf\``
指定bigsql的conf目录的位置，因为要加载相应的配置文件