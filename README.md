## 一、Redis基础

### 一、Redis入门

#### 一、Redis简介

##### 概念

redis是用c语言开发的开源的高性能键值对（key-value）数据库。

##### 特征

1、数据间没有必然的关联关系

2、内部采用单线程机制进行工作

3、高性能

4、多数据类型支持

5、持久化支持。可以进行数据灾难恢复。

##### 应用

为热点数据加速查询

任务队列，如秒杀、抢购、购票排队等

即时信息查询

时效性信息控制

分布式数据共享

消息队列

分布式锁

#### 二、redis的下载

linux（适用于企业级开发）

windows适用于初学：

启动服务端：redis-server.exe

启动客服端：redis-cli.exe

#### 三、redis的基本操作

set name xiaohong

get name

clear

help

### 二、数据类型

#### 一、数据存储类型介绍

作为缓存使用：

1、原始业务功能设计：秒杀；京东618；双11；排队购票

2、运营平台监控到的突发高频访问数据：突发时政要闻，被强势关注围观

3、高频、复杂的统计数据：在线人数

附件功能：

系统功能优化或升级：单服务器升级集群；session管理；token管理

redis数据类型（5种常用）：

| redis数据类型 | 类比java的类型 |
| ------------- | -------------- |
| string        | String         |
| hash          | HashMap        |
| list          | LinkedList     |
| set           | HashSet        |
| sorted_set    | TreeSet        |

#### 二、string

redis自身是一个Map，其所有数据都采用key:value的形式存储

数据类型指的是存储的数据的类型，也就是value部分的类型，key部分永远都是字符串

##### 1、string类型数据的基本操作

单个：

```
set name xiaohong
get xiaohong
del name
```

多个：

```
mset key1 value1 key2 value2 ...
mget key1 key2 ...
```

获取数据字符个数（字符串长度）

```
strlen key
```

追加信息到原始信息后部（如果原始信息存在就追加，否则新建）

```
append key value
```

##### 2、string类型数据的扩展操作

业务场景：

分表的时候，需要保证每张表的主键id不能重复。

oracle数据库具有sequence设定，可以解决该问题。

但是mysql数据库并不具有类似的机制，可以用mysql的自增来解决。

设置数值数据增加指定范围的值：

```
incr key
incrby key increment
incrbyfloat key increment
```

设置数值数据减少指定范围的值：

```
decr key
decrby key decrement
```

##### 3、说明

>string在redis内部存储默认就是一个字符串，当遇到增减类操作incr，decr时会转成数值型进行计算。
>redis所有的操作都是原子性的，采用单线程处理所有业务，命令都是一个一个执行的，因此无需考虑并发带来的数据影响。
>注意：按数值进行操作的数据，如果原始数据不能转成数值，或超越了redis数值上限范围（java中long类型的最大值，Long.MAX_VALUE），将报错
>
>
>
>tips:
>redis可用于控制数据库表主键id
>此方案适用于所有数据库，且支持数据库集群

##### 4、数据时效性设置

```
setex key 秒 value
psetex key 毫秒 value
```

#### 三、hash

##### 1、hash类型数据的基本操作

单个：

```
hset key field1 value1
hset key field2 value2
hget key field
hdel key field1 field2
```

多个：

```
hmset key field1 value1 field2 value2 ...
hmget key field1 field2 ...
```

其他：

```
hgetall key
hlen key	-- 获取哈希表中字段的数量
hexists key field -- 获取哈希表中是否存在指定的字段。0-没有，1-存在
```

##### 2、hash类型数据的扩展操作

获取所有的字段名或字段值

```
hkeys key
hvals key
```

设置指定字段的数值数据增加指定范围的值

```
hincrby key field increment
hincrbyfloat key field increment
```

##### 3、hash类型数据操作的注意事项

>hash类型下的value只能存储字符串，且不存在嵌套
>每个hash可以存储2<sup>32</sup>-1个键值对
>has类型十分贴近对象的数据存储形式，但不可将hash作为对象列表使用
>hgetall操作可以获取全部属性，如果内部field过多，遍历整体数据效率就会很低，有可能成为数据访问瓶颈

##### 4、应用场景

###### 购物车

hset user_id_01 goods_id_001:nums 3

因为每个用户都可能买这件商品，所以把商品信息放到独立hash：

hsetnx key field value -- 如果当前key的field有值就不修改，如果没值才赋值

###### 抢购

#### 四、list

##### 1、基本操作

底层：双向链表

添加/修改数据：

```
lpush key value1 value2 ... --左进
rpush key value1 value2 ... --右进
```

获取数据：

```
lrange key start stop -- stop为-1表示倒数第一个元素
lindex key index
llen key
```

获取并移除数据：

```
lpop key
rpop key
```

##### 2、扩展操作

规定时间内获取并移除数据【可以开两个客户端验证】

```
blpop key1 [key2] timeout -- 如果key有数据会直接读出来，如果没有，则会等待timeout
brpop key1 [key2] timeout
```

##### 3、业务场景

朋友圈点赞顺序

移除指定数据：

```
lrem key count value
```

可应用于有操作先后顺序的数据控制

##### 4、注意事项

>list中保存的数据都是string类型的，数据总容量有限，最多2 32-1个元素
>list具有索引的概念，但是操作数据时通常以队列的形式进行入队出队操作
>获取全部数据操作结束索引设置为-1
>list可以对数据进行分页操作，通常第一页的信息来自于list，第2页及更多的信息通过数据库加载
>
>
>
>tips：
>
>多台服务器操作日志的统一输出
>
>可应用于最新消息展示

#### 五、set

在查询方面效率更高（比hash）

##### 1、基本操作

添加数据：

```
sadd key member1 [member2]
```

获取全部数据：

```
smembers key
```

删除数据：

```
srem key member1 [member2]
```

获取集合数据总量：

```
scard key
```

判断集合中是否包含指定数据：

```
sismember key member
```

##### 2、扩展操作

随机获取集合中指定数量的数据：

```
srandmember key [count]
```

随机获取集合中的某个数据并将该数据移出集合：

```
spop key
```

应用：

随机推荐类信息检索，如热点歌单推荐、热点新闻推荐等

#### 六、sorted_set

在set的存储结构基础上添加可排序字段

#### 七、数据类型实践案例

### 三、通用命令

#### 一、key通用指令

##### 1、基本操作

删除指定key：

```
del key
```

获取key是否存在：

```
exists key
```

获取key的类型

```
type key
```

##### 2、扩展操作（时效性控制）

#### 二、数据库通用指令

### 四、Jedis

#### 一、Jedis简介

Java语言连接redis服务的途径：

Jedis

SpringData Redis

Lettuce

#### 二、HelloWorld（Jedis版）



#### 三、Jedis读写redis数据

#### 四、Jedis简易工具类开发

#### 五、可视化客户端

## 二、Redis高级

### 一、持久化

### 二、redis.conf

### 三、事务

### 四、集群

## 三、Redis应用

### 一、企业级解决方案