

#### 介绍
    java的一些坑和技巧，骚套路

#### 软件架构

    springboot+mybatis-plus


#### 需要环境

    1.  jdk8+maven3.0+
    2.  idea
    3.  mysql5.7+


#### rdbms和nosql对比
    我通过三个案例分别对比了缓存数据库 Redis、时间序列数据库 InfluxDB、搜索数据库 ES 和 MySQL 的性能。
    我们看到：

    1.Redis 对单条数据的读取性能远远高于 MySQL，但不适合进行范围搜索。
    2.InfluxDB 对于时间序列数据的聚合效率远远高于 MySQL，但因为没有主键，所以不是一个通用数据库。
    3.ES 对关键字的全文搜索能力远远高于 MySQL，但是字段的更新效率较低，不适合保存频繁更新的数据。
    4.最后，我们给出了一个混合使用 MySQL + Redis + InfluxDB + ES 的架构方案，充分发挥了各种数据库的特长，相互配合构成了一个可以应对各种复杂查询，以及高并发读写的存储架构。
    主数据由两种 MySQL 数据表构成，其中索引表承担简单条件的搜索来得到主键，Sharding 表承担大并发的主键查询。主数据由同步写服务写入，写入后发出 MQ 消息。
    辅助数据可以根据需求选用合适的 NoSQL，由单独一个或多个异步写服务监听 MQ 后异步写入。由统一的查询服务，对接所有查询需求，根据不同的查询需求路由查询到合适的存储，确保每一个存储系统可以根据场景发挥所长，并分散各数据库系统的查询压力。




#### OkCache
    OkCache
    一个高性能二级缓存实现, 内存LRU缓存 + 磁盘文件持久化缓存。
    
    支持过期(Expiration)清除；
    支持LRU ~ 如果超过内存缓存容量，最近不常使用的项将被剔除(Eviction)；
    支持剔除(Eviction)到二级可持久化缓存(BigCache)；
    支持回写(Write Behind)到后端持久化存储，例如DB。
    BigCache的Key常驻内存，Value可持久化。
    BigCache支持纯磁盘文件，内存映射+磁盘文件，和堆外内存+磁盘文件三种模式。

    注意
        运行BigCache单元测试前，请将TestUtil.TEST_BASE_DIR修改为本地测试目录：
        回写到MySQL的代码未开源，需要的自己实现BehindStorage接口即可。
#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


