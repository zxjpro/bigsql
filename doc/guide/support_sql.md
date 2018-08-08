# 支持的SQL

- [x]  ``SELECT [FIELD] FROM TABLE_NAME``
- [x]  ``SELECT [FIELD] FROM TABLE_NAME WHERE [CONDITION]``


## GROUP BY

:::tip
group by 的语句中，只能查询两个字段，这两个字段，一个是查询函数，比如MAX(),COUNT()，另一个则必须是group by的字段
:::


> group by 所支持的函数有  ``COUNT`` ``MAX``  ``MIN``  ``SUM``  ``AVG`` 

- [x]  ``SELECT COUNT(1),[FIELD] FROM TABLE_NAME WHERE [CONDITION] GROUP BY [FIELD]``
- [x]  ``SELECT MAX(1),[FIELD] FROM TABLE_NAME WHERE [CONDITION] GROUP BY [FIELD]``
- [x]  ``SELECT MIN(1),[FIELD] FROM TABLE_NAME WHERE [CONDITION] GROUP BY [FIELD]``
- [x]  ``SELECT SUM(1),[FIELD] FROM TABLE_NAME WHERE [CONDITION] GROUP BY [FIELD]``
- [ ]  ``SELECT AVG(1),[FIELD] FROM TABLE_NAME WHERE [CONDITION] GROUP BY [FIELD]``

- [] 同时有GROUP BY 和 ORDER BY 


## ORDER BY

在使用``ORDER BY``时，查询字段中，必须包含排序字段。

比如这条SQL，用在单机数据库没有问题
```sql
SELECT name FROM person ORDER BY age
```
但是在bigsql中，就要改成这样，否则bigsql无法完成排序
```sql
SELECT name,age FROM person ORDER BY age
```

:::tip
NULL字段，将会排在最后面
:::

:::warning
使用了``ORDER BY``时，查询的字段不能为``*``
:::
