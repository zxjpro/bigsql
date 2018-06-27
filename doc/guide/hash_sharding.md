# 哈希切片
这里的哈希分表是单键分片，bigsql内置的哈希分片策略，支持分表，同时也支持分库分表。


## 小提示

::: warning 选择一个好的分片键
哈希切片，从名字上我们就可以得知，这是根据哈希值的一个分片，所以我们对于分片键的选择尤其重要，因为我们要避免哈希倾斜，如果哈希倾斜太严重，那么会非常大的影响分片效率。
所以我们需要选择一些内容并不是太固定的值进行分片，尽可能的让数据均衡的分布在各个分表中。
:::

::: danger 尽可能的使用分片键
为了提升查询效率，我们需要尽我们最大的可能，在查询的时候带上分片键，因为这样bigsql能准确的定位到一个物理数据源，并且执行相关操作，否则bigsql只能进行全表扫描，
在一个拥有着大量分表，并且数据量极其庞大的情况下，全表扫描简直是一个灾难！
:::




## 配置内容

我们进入bigsql/conf/schema/中，创建一个数据库文件夹，然后创建一个逻辑表的配置，配置信息如下，其实我们也可以参考bigsql/template/tableName.xml文件

```xml
<rule>
    <strategy>SingleColumnHashShardingStrategy</strategy>
    
    <properties>

        <property>
            <key>dataSource</key>
            <value>dataSourceName0,dataSourceName1,dataSourceName2</value>
        </property>
        
        <property>
            <key>shardingColumn</key>
            <value>columnName</value>
        </property>

        <property>
            <key>shardingNumber</key>
            <value>8</value>
        </property>
    </properties>
</rule>
```

## 配置说明

其实我们可以看到，其实整个配置文件分为两部分，第一部分就是声明规则名称，第二部分就是key-value对

```xml
<rule>
    <!-- 这一行代表着使用的策略名称，一般来说，策略是默认使用当前类名作为策略名称，
    当然，如果是自定义的策略插件，也可以对它进行覆盖 -->
    <strategy>SingleColumnHashShardingStrategy</strategy>
    
    <!-- 这下面的全部都是键值对了 -->
    <properties>
    
        <!-- 配置物理数据源列表，这里是分库分表，
        如果只有一个数据源，那就是不分库，只分表 -->
        <property>
            <key>dataSource</key>
            <value>dataSourceName0,dataSourceName1,dataSourceName2</value>
        </property>
        
        <!-- 配置分片的键的列名 -->
        <property>
            <key>shardingColumn</key>
            <value>columnName</value>
        </property>

        <!-- 分为多少个表，这里拆分为8个表 -->
        <property>
            <key>shardingNumber</key>
            <value>8</value>
        </property>
    </properties>
</rule>
```

如上方的配置，我们定义了3个数据源，并且定义了8个分表，如果我们定义的是9个分表，理所应当的，每个数据源有着3个分表，
但是我们现在使用了8个分表，它的分布情况如下：

- dataSourceName0 : table_1,table_2,table_3
- dataSourceName1 : table_4,table_5,table_6
- dataSourceName2 : table_7,table_8

:::tip 提示
数据源的命名是没有限制的，配置文件中可以随意命名。
但是物理表的命名，却是有规则的，我们需要以逻辑表的名称为开始，然后加入下划线，加上分表编号为后缀，值得注意的是，这里的编号是从1开始
:::

:::warning 注意
物理表需要我们自己去创建，bigsql并不会帮我们创建
:::



