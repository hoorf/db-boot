# db-boot
## 功能
+ 数据分库分表数据迁移

## 分库分表迁移使用
1. 单表迁移

迁移数据到新库,或者新表中
```yaml
dataSources:
  oldDb:
    url: jdbc:mysql://127.0.0.1:3306/bigdata?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  newDb_1:
    url: jdbc:mysql://127.0.0.1:3306/ds_1?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
tables:
  t_user_sharding:
    source:
      pk: id
      dataSourceName: oldDb
      tableName: t_user
    target:
      tableName: t_user
      dataSourceName: newDb_1
```

2. 分库分表处理

根据规则,划分不同库,不同表

```yaml
dataSources:
  oldDb:
    url: jdbc:mysql://127.0.0.1:3306/bigdata?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  newDb_0:
    url: jdbc:mysql://127.0.0.1:3306/ds_0?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  newDb_1:
    url: jdbc:mysql://127.0.0.1:3306/ds_1?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
tables:
  t_user_sharding:
    source:
      pk: id
      dataSourceName: oldDb
      tableName: t_user 
    target:
      tableName: t_user_${0..1}
      tableStrategy:
        type: INLINE
        props:
          shardingColumns: id
          algorithm-expression: t_user_${id % 2}
      dataSourceName: newDb_${0..1}
      dataSourceStrategy:
        type: INLINE
        props:
          shardingColumns: id
          algorithm-expression: newDb_${id % 2}


```

3. 轮循迁移表,平均分配数据到不同库表

```yaml
dataSources:
  oldDb:
    url: jdbc:mysql://127.0.0.1:3306/bigdata?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  newDb_0:
    url: jdbc:mysql://127.0.0.1:3306/ds_0?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  newDb_1:
    url: jdbc:mysql://127.0.0.1:3306/ds_1?serverTimezone=UTC&rewriteBatchedStatements=true
    driverClass: com.mysql.cj.jdbc.Driver
    username: root
    password: root
tables:
  t_user_sharding:
    source:
      pk: id
      dataSourceName: oldDb
      tableName: t_user
    target:
      tableName: t_user_${0..1}
      tableStrategy:
        type: INLINE
        props:
          shardingColumns: id
          algorithm-expression: t_user_${id % 2}
      dataSourceName: newDb_${0..1}
      dataSourceStrategy:
        type: ROUND
        props:
          roundNames: newDb_0,newDb_1


```