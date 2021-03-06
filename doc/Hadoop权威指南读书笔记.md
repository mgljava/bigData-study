## 第10章 管理Hadoop
#### HDFS
1. namenode和datanode数据存储目录
2. 安全模式

#### 工具使用
1. dfsadmin: 在HDFS上执行管理操作
2. fsck: 检查HDFS中文件地健康状况
3. datanode 块扫描器
4. 均衡器： start-balancer.sh 启动均衡器

### 监控
##### 日志
##### 度量
1. JVM度量
2. DFS
3. RPC
4. mapred
##### JMX

### 维护
1. 元数据备份（namenode）
2. 数据备份（datanode）
3. 文件系统检查
4. 文件系统均衡器

### 添加和删除节点
1. 添加节点
  - 配置datanode，并更新到namenode，执行命令：hadoop dfsadmin -refreshNodes
  - 配置nodemanager，并更新到RM中，执行命令：hadoop mradmin -refreshNodes
  - 配置slaves文件，以便后续管理
  - 启动datanode和nodemanager
  - 执行均衡器

2. 删除节点，需要配置include和exclude文件
  - 将需要被删除地节点地地址添加到exclude文件中，不更新include文件
  - 更新namenode：hadoop dfsadmin -refreshNodes
  - 更新RM：hadoop mradmin -refreshNodes
  - 此时namenode会把节点的信息复制到其他datanode节点
  - 在include文件中移除这些节点，并执行hadoop dfsadmin -refreshNodes和hadoop mradmin -refreshNodes命令
  - 从slaves文件中移除节点
 
### 升级
1. 升级前执行fsck
2. 升级过程
  - 关闭MapReduce，中止NodeManager
  - 关闭HDFS，备份namenode的目录
  - 安装新版的HDFS和MapReduce
  - 使用-upgrade选项启动HDFS
  - 等待升级完成
  - 检查HDFS是否运行正常
  - 启动MapReduce
  - 回滚或升级成功
3. 回滚：$HADOOP_HOME/sbin/start-dfs.sh -rollback
4. 定妥升级：$HADOOP_HOME/bin/hadoop dfsadmin -finalizeUpgrade

## 第11章 Pig
### Pig概念
1. 用于描述数据流的语言，称为Pig Latin
2. 用于运行Pig Latin程序的执行环境，当前有两个环境，单JVM的本地环境和Hadoop集群上的分布式环境

##### Pig Latin
1. Pig Latin由一系列的“操作”和“变换”组成
2. 在Pig内部，将这些操作和变换转换为一系列的MapReduce作业
3. grunt: grunt是与pig进行交互的外壳程序

##### 执行类型
1. 本地模式：pig -x local
2. MapReduce模式：pig 直接执行，默认为MapReduce模式

##### 运行Pig程序
1. 脚本: 直接运行pig代码，如 pig script.pig
2. Grunt: 如果没有指明pig要运行的文件，或者没有使用-e选项，pig就会启动grunt，在grunt中，可以通过run和exec命令来运行pig脚本
3. 嵌入式方法：可以通过PigServer类来运行Pig程序。或者通过PigRunner以编程的方式来使用grunt

##### Pig Latin程序设计
1. 结构：一条语句理解为一个操作
2. 语句：Pig Latin程序运行时，每个命令按次序进行解析，如果遇到错误，解释器中止运行。解释器会给每个关系操作建立“逻辑计划”，并不会真正的执行，只有遇到STORE命令或者DUM命令才会编译为物理执行
3. 表达式
  - 常数: 1.0 'a'
  - 字段$0: 第0个位置的元素
  - 投影：c.f records.year
  - Map查找: m#k records.year#'2012'
  - 算术: +-*/% 
  - 条件：x ? y : z
  - 比较：> < = != (x is null)(x is not null)
  - 布尔：(x or y) (x and y) (not x)
  - 函数：isGood(quality)
  - 平面化：FLATTEN(f),从包和元组中去除嵌套, FLATTEN(group)
4. 基本类型
  - int
  - long
  - float
  - double
  - bytearray,对应Java的byte数组
  - chararray,对应Java的String类型
5. 复杂类型
  - tuple：任何类型的字段序列
  - bag：元组的无序的多重集合
  - map：键值对的集合
##### Pig Latin函数
1. 计算函数：获取一个或多个表达式作为输入，并返回另一个表达式
  - AVG
  - CONCAT
  - SUM
  - MAX
  - COUNT等等
2. 过滤函数：返回一个boolean值，被FILTER操作用于移除不需要的行
  - ISEmpty
3. 加载函数：指明如何从外部存储加载数据到一个关系
4. 存储函数：指明将一个关系的数据存储到外部存储
  - PigStorage
  - BinStorage
  - TextLoader
  - JsonLoader
  - HBaseStorage

##### 宏
1. 定义宏：DEFINE
2. 使用宏

##### 用户自定义函数
1. 定义函数
2. 编写函数
3. 打包、编译
4. 注册jar包到pig得搜索路径上
5. 取别名：DEFINE isGood com.github.mgljava.pig.IsGoodQuality();
6. 自定义过滤函数： com.github.mgljava.pig.IsGoodQuality();
7. 自定义计算函数： com.github.mgljava.pig.Trim();
8. 动态调用：InvokeForString、InvokeForInt、Long、Double、Float等等
9. 动态调用的用法：DEFINE trim InvokeForString('org.apache.commons.lang.StringUtils.trim', 'String'); 第一个参数代表调用的方法是哪个类下的哪个方法，第二个参数是返回值的类型
10. 自定义加载函数

### 数据处理操作
##### 数据加载和存储
##### 数据过滤
1. FOREACH...GENERATE: b = FOREACH a GENERATE $0+2,'Const';
2. STREAM
3. 数据分组和连接
  - join：C = JOIN A BY $0, B BY $1
  - COGROUP：创建一个嵌套的输出元组集合：D = COGROUP A BY $0, B BY $1;
  - CROSS：对所有元组进行连接：I = CROSS A,B
  - GROUP：对元组进行分组：B = GROUP A BY SIZE($1);
4. 数据排序：B = ORDER A BY $0, $1 DESC;
5. 数据组合和切分
  - UNION：C = UNION A, B
  - SPLIT：UNION的反向操作，拆分元组

### Pig实战
1. 并行处理，设置Reduce的个数。Map的个数由输入分片决定
2. 参数代换：$input, $output. 在运行脚本时直接采用 pig -param input=/ -param output=/ code/script.pig
3. -param_file
4. 当前系统日期：-param output=/tmp/`date "+%y-%m-%d" `/out
5. -dryrun模式

## 第12章 Hive
### 安装Hive 2.3.7版本
0: 安装范本：https://www.jianshu.com/p/2633be68177f  
1. 下载Hive
2. 解压hive
3. 配置环境变量
4. 先使用命令初始化schema，`schematool -dbType derby -initSchema`，该命令会创建 metastore_db 目录
5. 使用hive命令进入hive交互式环境

### Hive索引
1. 紧凑索引
2. 位图索引

### Hive锁
1. 表级锁
2. 分区级锁

### HiveQL
##### 数据类型
1. 基本数据类型：int float boolean double
2. 复杂数据类型：array map struct

### 表
1. Hive的表在逻辑上由存储的数据和描述表中的数据形式的相关元数据组成
2. hive把元数据放到metastore中

##### 表的形式
1. 托管表：创建表时，默认情况下Hive负责管理数据，意味着hive把数据移入它的"仓库目录",例如在HDFS上有一个文件 /internal.txt，如果采用托管表，那么在LOAD命令之后就会把该文件移动到Hive所管理的目录下。 /user/hive/warehouse
2. 外部表：让Hive到仓库目录以外的位置访问数据

##### 分区
1. 分区：一个表（如日志表）可以依照多个维度来进行分区，如时间、位置等等，还可以进行子分区，即根据时间分区后，再根据国家来进行分区
2. 创建分区：分区是在创建表时通过关键字 PARTITIONED BY 语句定义：CREATE TABLE logs(ts BIGINT, line STRING) PARTITIONED BY (dt STRING, country STRING);
3. 加载数据到分区：LOAD DATA LOCAL '/data/sample.txt' OVERWRITE INTO TABLE logs PARTITION (dt='2001-01-01', country='GB')

##### 桶
1. 创建桶：关键字 CLUSTERED BY实现：CREATE TABLE bucketed_users(id INT, name STRING) CLUSTERED BY(id) INTO 4 buckets;
2. 计算桶：针对以上的创建表的请求，利用用户ID来确定如何划分桶（HIVE对值进行哈希并将结果除以桶的个数取余数）

##### 存储格式
1. 行格式：指明一行中的字段如何存储
2. 文件格式：指一行中字段容器的格式，最简单的格式是纯文本文件

##### CREATE TABLE ... AS SELECT
将查询的输出结果存放到新的表采用 CTAS

##### 查询数据
1. 内连接查询：JOIN ... ON
2. 外连接查询：LEFT OUTER JOIN ... ON
3. 半连接查询：LEFT SEMI JOIN ... ON
4. map链接：select t.*, a.* from tab1 t JOIN tab2 a ON(t.id = a.id)
5. 子查询：通过SELECT嵌套查询，支持有限，只能出现在SELECT语句的FROM子句中

##### 视图
和MySQL视图类似，逻辑表示

### 用户自定义函数(UDF)
1. 必须采用Java编写
2. 必须是UDF的子类
3. 一个UDF必须至少实现了evaluate()方法，可以有多个，Hive在执行的时候会找到匹配的

##### 函数分类
1. 普通UDF：操作用于单行，并输出一行数据
2. 用户定义聚集函数：接受多个数据输入行，产生一个数据输出行
3. 用户定义表生成函数：作用于单个数据输入行，产生多个数据行->一个表作为输出

##### 使用UDF
1. 打成Jar包
2. 在Hive环境下 ADD JAR /root/your-jar.jar
3. 起别名：CREATE TEMPORARY FUNCTION strip AS 'com.github.mgljava.hive.function.Strip'，该别名在单个会话中有效，如果需要在每个会话中使用，可以采用在主目录创建 .hiverc 文件
4. 使用UDF：SELECT strip('bee') FROM dummy;

## 第13章 HBase
HBase是一个在HDFS上开发的面向列的分布式数据库，如果需要实时的随机访问超大规模的大数据集，就可以使用HBase

### 安装HBase
1. 下载安装包
2. 解压
3. 配置HBASE_HOME
4. 启动HBase  $HBASE_HOME/bin/start-hbase.sh 可以在16010端口访问HBase

##### HBase shell

##### HBase优点
1. 没有真正的索引
2. 自动分区
3. 线性扩展和对于新节点的自动处理
4. 普通商用硬件支持
5. 容错
6. 批处理

##### HBase常见问题
1. 由于HBase在处理时，文件描述符总是被打开的来节约访问时间，所以会出现文件描述符被用完的情况，Linux默认1024，通过ulimit来修改
2. datanode上的线程用完，线程限制默认为256
3. Sync：必须在有可用的sync的HDFS上使用HBase

##### HBase的模式设计
1. 连接
2. 行键

## 第14章 Zookeeper
Zookeeper是Hadoop的分布式协调服务，由于会出现部分失败，不过Zookeeper能对部分失败进行正确处理

### Zookeeper特点
1. Zookeeper是简单的，Zookeeper的核心是一个精简的文件系统，它提供一些简单的操作和一些额外的抽象的操作，例如：排序和通知
2. Zookeeper是富有表现力的，Zookeeper的基本操作是一组丰富的"构建"，可用于实现多种协调数据结构和协议，例如：分布式队列，分布式锁和Leader的选举
3. Zookeeper具有高可用性：Zookeeper运行在一组机器上，在设计上具有高可用性，可以避免单点故障
4. Zookeeper采用松耦合交互方式
5. Zookeeper是一个资源库

### Zookeeper服务
Zookeeper是一个具有高可用性的高性能协调服务

##### 数据模型
1. Zookeeper维护着一个树形层次结构，树中的节点被称为znode，znode可以用于存储数据，并且有一个与之关联的ACL（访问权限），一个znode能存储的数据被限制在1MB以内
2. Zookeeper的数据访问具有原子性，读写操作都是要么全部成功，要么全部失败，不会出现部分失败的情况
3. 路径访问必须以 / 开头的绝对路径
4. znode的类型有短暂型和持久型，znode的类型在创建之后就不能再更改。短暂的znode不允许有子节点，并且在会话结束时删除
5. 顺序号：可用于实现共享锁
6. 观察：znode发生变化时，观察机制可以让客户端得到通知，观察只能触发一次，如果被触发了，需要重新注册观察
7. 观察触发器

##### 操作
Zookeeper含有9种基本操作
1. create：创建一个znode（父节点必须存在）
2. delete：删除一个znode（该znode不能有任何子节点）
3. exists：测试一个znode是否存在并且查询它的元数据
4. getACL,setACL：获取/设置一个znode的ACL
5. getChildren：获取一个znode的子节点列表
6. getData,setData：获取/设置一个znode所保存的数据
7. sync：将客户端的znode视图与Zookeeper同步

##### 更新
1. 更新操作是原子的
2. mutil操作更新是要么全部成功，要么全部失败

##### 实现
1. 集群模式有单机和集群两种
2. 如果是集群提供服务，那么半数以上服务可用，那么整体就是可用的
3. 确保所有修改操作都是通过复制到集群中半数以上的机器上

##### ZAB协议
1. 阶段1：领导者选举：所有机器通过一个选择过程来选出leader和follower，一旦半数以上的follower已经将其状态与leader同步，那么这个阶段就已经完成
2. 阶段2：原子广播：所有的写操作都会转发给leader，再由leader转发给follower，当半数以上的follower已经将修改持久化后，这个操作才会被更新。这个类似于2PC协议
3. Zookeeper没有采用Paxos算法

##### 一致性
1. 顺序一致性：来自客户端的所有更新请求都会按照其发送顺序提交
2. 原子性：所有操作要么成功，要么失败
3. 单一系统镜像：客户端无论连接到哪一个视图，看到的都是同样的视图
4. 持久性：一旦更新操作成功，那么结果就不会被撤销，并且不会受到故障的影响
5. 及时性

##### ZK异常
1. InterruptedException：当出现中断异常时，需要调用线程中断去中断当前线程，以便取消操作
2. KeeperException：所有ZK异常的父类，它的子异常有：NoNodeException等等
  - 状态异常：如操作znode失败
  - 可恢复异常：如链接过时，需要重新链接
  - 不可恢复异常：如身份验证失败






















