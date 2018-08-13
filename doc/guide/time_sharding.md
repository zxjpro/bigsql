# 时间分片

## 小提示

:::danger 时间分片不支持全表扫描
出于性能以及安全方面的考虑，bigsql并不打算支持时间分片的全表扫描。

我们可以试想一下，一个已经运行了一两年的逻辑表，到底会存在多少数据，如果我们写出``SELECT * FROM table_name``这样的SQL，
那么对于bigsql来说，将是难以想象的压力。
:::

:::warning 分片时间必须指定区域
bigsql要求查询条件中，时间字段，必须给定一个区域，比如``BETWEEN AND``，或者``time > [startTime] and time < [endTime]``，或者``time = [timeValue]``。

其实这样做的原因可以理解，如果我们写了这样一个条件``time > [timeValue]``，那么随着时间的增长，分片表会越来越多，就意味着定位到的分片表会越来越多，查询的复杂度也就会越来越多，而对于一个透明的中间件来说，这是开发人员很容易忽略掉的。
:::

:::tip 提示
分片的时间参数，不能小于定义的最小分片值。比如我们定义的最小边界为2018年，那么我们就不能插入2018年以前的数据。

两个区间值，不能相等，比如``time >= '2018-01-01' and time <= '2018-01-01'``，如果相等，则直接使用``time = '2018-01-01'``

查询条件给定的区域中，最大不能超过12个分片表，也就是说，如果你是按月分表，那么你的查询条件中，最多查询12个月的数据，也就是一年。
:::


## 配置内容

我们进入bigsql/conf/schema/中，创建一个数据库文件夹，然后创建一个逻辑表的配置，配置信息如下，其实我们也可以参考
bigsql/template/time_sharding.xml文件

```xml
<rule>
    <!-- the strategy name -->
    <strategy>StandardTimeShardingStrategy</strategy> <!-- standard time sharding -->
    <properties>


        <!-- sharding by time format -->
        <property>
            <key>shardingFormat</key>

            <!-- YEAR,MONTH,DAY   -->
            <value>MONTH</value>
        </property>


        <!-- config the sharding column name -->
        <property>
            <key>shardingColumn</key>
            <value>create_time</value>
        </property>



        <!-- config the time range in dataSource ...start  -->


        <!--[yyyyMMdd-yyyyMMdd]=dataSourceName -->
        <!-- start <= shardingTime < end -->
        <!-- the date range,will be save dataSource1 -->
        <!-- we can config multipart range by ; -->
        <!-- the multipart range is must order by date asc , bigsql get the first match dataSource-->
        <property>
            <key>range</key>
            <value> [20170101-20180101]=dataSource1   ;   [20180101-20180501]=dataSource2   ;   [20180301-?]=dataSource3 </value>
        </property>


        <!--[yyyyMMdd-?]=dataSourceName -->
        <!-- ? is the future time  -->


        <!-- config the time range in dataSource ...end  -->

    </properties>
</rule>
```


## 配置说明

同样的，配置文件，声明了分片策略名称，后面就是具体的key-value信息


```xml
<rule>
    <!-- 策略名称，这个策略名称代表着标准时间分片 -->
    <strategy>StandardTimeShardingStrategy</strategy> <!-- 标准时间分片 -->
    <properties>


        <!-- 按时间分片的颗粒度 -->
        <property>
            <key>shardingFormat</key>

            <!-- 这里有三种格式，分别是年，月，日 ，对应着关键字 YEAR,MONTH,DAY   -->
			<!-- 如下就是按月分片 -->
            <value>MONTH</value>
        </property>


        <!-- 设置分片键，这里是按照create_time来分片 -->
        <property>
            <key>shardingColumn</key>
            <value>create_time</value>
        </property>



        <!-- 配置时间分区区域...开始  -->


        <!--[yyyyMMdd-yyyyMMdd]=dataSourceName -->

        <!-- 分片开始时间 <= 传入的分片时间 < 分片的结束时间 -->
        <!-- 等号后面的是该分片区域的数据源名称 -->
        <!-- 我们可以配置多个分片区域，以 ; 隔开 -->
        <!-- 多个分片区域，必须按时间的正序排序 , 因为bigsql会获取最先定位到的range-->
        <property>
            <key>range</key>
            <value> [20170101-20180101]=dataSourceName1   ;   [20180101-?]=dataSourceName2</value>
        </property>


        <!--[yyyyMMdd-?]=dataSourceName -->
        <!-- 问号代表着往后的所有时间，如果哪天数据库的数据量过多，到了需要增加数据库的时间，就把问号改为具体的时间，然后再增加一个区域  -->


        <!-- 配置时间分片区域 ...结束  -->

    </properties>
</rule>
```

我们重点看一下range的配置。

比如我们有``[20170101-20180101]=dataSourceName1   ;   [20180101-20190101]=dataSourceName2 ; [20190101-?]=dataSourceName3``


代表着2017年的数据，会存在数据源``dataSourceName1``中，而2018年的数据，会存在``dataSourceName2``中，2019年后的数据，会存在``dataSourceName3``中。
问号代表着未来的所有时间。当然如果到了2010年，需要再增加一个数据库的时候，我们又可以再增加一个时间段的分片，就可以满足扩容了，比如改成
``[20170101-20180101]=dataSourceName1   ;   [20180101-20190101]=dataSourceName2 ; [20190101-20200101]=dataSourceName3 ; [20200101-?]=dataSourceName4``


## 分片表的创建

> 分片表，就是一个物理表的名称，它是真实的存放在mysql实例中的表

:::tip 提示
分片表需要手工创建，我们可以一次性创建未来几个月可用的分片表。但是创建的时间，需要按照配置的range，创建在不同的数据库中。每个分片表的表结构必须一致。
:::

那么bigsql的逻辑表，是如何与分片表对应起来的呢？假设我们的逻辑表名为``person``

### 按年分表

|时间|分片表名|
|-|-|
|2016年|person_2016|
|2017年|person_2017|
|2018年|person_2018|


### 按月分表

|时间|分片表名|
|-|-|
|2016年1月|person_201601|
|2016年11月|person_201611|
|2018年5月|person_201805|


### 按天分表

|时间|分片表名|
|-|-|
|2016年1月1日|person_20160101|
|2016年10月11日|person_20161011|
|2018年9月11日|person_20160911|