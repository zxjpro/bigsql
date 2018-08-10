# 异常处理

## 抛出异常

在bigsql处理mysql协议的过程中（不包含启动过程），我们只能抛出``com.xiaojiezhu.bigsql.common.exception.BigSqlException``及其子类的异常，不允许抛出其它异常。

``com.xiaojiezhu.bigsql.common.exception.BigSqlException``中的异常中带有一个错误码


## 错误码

对于特定情况的异常，我们需要给出特定的错误码，以方便其它位置处理它。

### 1 开头的错误码，是配置级别的错误

|错误码|说明|异常类|
|-|-|-|
|100|配置文件类错误||
|101|从配置文件载数据源失败|``com.xiaojiezhu.bigsql.common.exception.DataSourceLoadException``|
|102|加载逻辑表规则失败|``com.xiaojiezhu.bigsql.common.exception.RuleParserException``|
|103|XML配置解析错误|``com.xiaojiezhu.bigsql.common.exception.XmlParseException``|
|104|数据源不存在|``com.xiaojiezhu.bigsql.common.exception.DataSourceNotExistsException``|
|105|找不到配置文件|``com.xiaojiezhu.bigsql.common.exception.ConfigNotExistException``|
|106|找不到数据库的配置|``com.xiaojiezhu.bigsql.common.exception.DatabaseNotFoundException``|


### 2 开头的错误码，是SQL级别的错误

|错误码|说明|异常类|
|-|-|-|
|200|sql处理类错误||
|201|SQL中指定了分布式主键的列，指定的值为NULL|``com.xiaojiezhu.bigsql.common.exception.IncrementColumnNullException``|
|202|SQL语句中出现全表扫描|``com.xiaojiezhu.bigsql.common.exception.FullScanTableException``|
|203|SQL语句中不存在分片键|``com.xiaojiezhu.bigsql.common.exception.ShardingColumnNotExistException``|
|204|SQL解析错误|``com.xiaojiezhu.bigsql.common.exception.SqlParserException``|

### 3 开头的错误码，是系统级别的错误码

|错误码|说明|异常类|
|-|-|-|
|300|系统级别错误||
|301|协议包错误|``com.xiaojiezhu.bigsql.common.exception.ProtocolErrorException``|
|302|执行SQL语句出现异常|``com.xiaojiezhu.bigsql.common.exception.InvokeStatementException``|
|303|执行了不支持的操作|``com.xiaojiezhu.bigsql.common.exception.NotSupportException``|
|303|执行了不支持的操作|``com.xiaojiezhu.bigsql.common.exception.NotSupportException``|
|303|合并resultSet错误|``com.xiaojiezhu.bigsql.common.exception.MergeException``|
|304|SQL不支持错误，与SQL解析错误的同不的地方是，SQL解析错误是语法错误|``com.xiaojiezhu.bigsql.common.exception.SqlNotSupportException``|
|305|操作事务异常|``com.xiaojiezhu.bigsql.common.exception.TransactionException``|


### 4 开头的错误码，是外部应用的错误吗，如连不第物理数据库

|错误码|说明|异常类|
|-|-|-|
|400|外部系统错误||