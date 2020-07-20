## Spark
Spark是分布式计算框架，底层操作的就是RDD

### Spark任务组成
application => job => stage(如果失败，重试4次) => task(如果失败，重试3次)
RDD Object => DAGScheduler => TaskScheduler => Worker

### Spark 与 MapReduce的区别
1. Spark的迭代基于内存，MR基于磁盘
2. Spark有DAG(有向无环图)执行引擎，执行速度快

### Spark运行模式
1. local：用于本地开发，多用于测试
2. standalone：Spark自带的资源调度框架，支持分布式搭建，Spark可以基于Standalone计算
3. yarn：Hadoop生态圈的资源调度框架，Spark支持在Yarn中运行
4. mesos：Apache资源调度框架

### Spark代码编写流程
1. 创建SparkConf，配置master，appName
2. 创建SparkContext（new SparkContext(conf)）对象sc（通往集群的唯一通道）
3. 通过sc对象获取RDD对象
4. 对得到的RDD对象使用 Transform类算子进行数据转换
5. 使用 Action算子对 Transform类算子触发执行
6. 关闭sc（sc.stop()）

### application执行慢
1. 有没有数据倾斜
2. 有没有开启推测执行

### Spark资源申请
##### 粗粒度资源申请（Spark）
在application执行之前，会将所有资源申请完毕，申请到资源就执行application，申请不到就一直等待，所有的task执行完毕才会释放资源
1. 优点
2. 缺点

-- 待补充 TODO

##### 细粒度资源申请（MR）

### RDD
RDD(Resilient Distributed Dataset), 弹性分布式数据集
##### RDD的五大特性
1. RDD是由一系列partition组成
2. 算子(函数)是作用在partition上的
3. RDD之间有依赖关系
4. 分区器是作用在K.V格式的RDD上
5. partition对外提供最佳的计算位置，利于数据处理的本地化

##### RDD操作(JoinTest)
1. join：按照两个RDD的key去关联
2. leftOuterJoin：以左为主
3. rightOuterJoin： 以右为主
4. fullOuterJoin 两边连接

##### 算子
理解为函数，例如 map、flatmap、count等等都是算子
1. Transformation：懒执行，需要action算子触发执行
2. Action：触发Transformation类算子执行，一个application中有几个Action算子，就有几个job
3. 持久化算子：cache、persist、checkpoint

##### 补充算子
1. mapPartitionWithIndex: 带分区号的transform算子
2. repartition:  从新分区，可以增加也可以减少，是将原来的rdd1重新分区为rdd2，两个rdd之间是宽依赖，有shuffle
3. coalesce(numberPartition:Int, shuffle:Boolean):  numberPartition分区数，shuffle是否产生shuffle，默认为false 
4. groupByKey: 针对二元组，根据key进行分组
5. zip：针对两个KV搁置的rdd进行一对一压缩，所以必须保证两个RDD的每个分区数量是一样的
6. zipWithIndex：给RDD中的每个元素与当前元素的下标压缩成一个KV格式的RDD
7. countByKey: 对RDD中相同的key的元素计数
8. countByValue: 对RDD中相同的元素计数，对整条数据计数
9. reduce: 对RDD中的每个元素使用传递的逻辑去处理

##### RDD 的持久化
1. cache：将数据缓存在内存中，懒执行算子，需要action触发
2. persist: 手动指定持久化级别(StorageLevel)，懒执行算子，需要action触发
3. checkpoint的功能：懒执行
  - 将数据存在磁盘中
  - 切断RDD之间的依赖，可以通过checkpoint的数据进行恢复
  - checkpoint的数据可以永久存储到磁盘上，而cache和persist在程序运行完了就销毁了
  - 有些场景必须 checkpoint
  - 对某个rdd进行checkpoint之前最好cache前一个rdd，避免从头进行计算
4. cache和persist的注意点
  - cache和persist都是懒执行，需要action触发
  - 对一个RDD进行cache或是persist之后可以赋值给一个RDD变量，这个变量就是持久化后的变量
  - 这个变量后不能直接跟 action算子，如 rdd1.cache().count()
5. cache、persist、checkpoint持久化的单位都是 partition
6. checkpoint执行流程
  - 当application有action触发执行时，job执行完成之后，会从前回溯 
  - 回溯去找有哪些RDD被checkpoint，对checkpoint的做标记
  - 回溯完成后，重新计算checkpoint RDD的数据，将结果写入checkpoint目录中
  - 切断RDD的依赖关系
  - 优化：对RDD checkpoint之前，最好先cache下
  
##### RDD的窄依赖和宽依赖
1. RDD窄依赖
  - 父RDD与子RDD partition之间的关系是一对一
  - 父RDD与子RDD partition之间的关系是多对一
2. RDD宽依赖（有shuffle）：父RDD与子RDD partition之间的关系是一对多
3. 有shuffle就会有落地磁盘，就会有IO操作
  
##### Stage
1. stage由一组并行的task组成
2. stage的并行度由final RDD的partition决定
3. 如何提高stage并行度（增加shuffle类算子的分区数）：增加final RDD partition个数
4. stage步骤中，管道中的数据何时落地？
  - shuffle write
  - 对RDD进行持久化：cache、persist、checkpoint

### Spark 运行模式和提交任务
##### standalone
运行模式
1. client
  - 提交命令：`./spark-submit --master spark://vm01:7077 --deploy-mode client  --class org.apache.spark.examples.SparkPi ../examples/jars/spark-examples_2.11-2.4.6.jar 100`
  - 步骤1：在客户端提交Application，Driver在客户端启动
  - 步骤2：客户端向Master申请资源，Master返回Worker节点
  - 步骤3：Driver向Worker节点发送task，监控task执行，回收结果
2. cluster
  - 提交命令：`./spark-submit --master spark://vm01:7077 --deploy-mode cluster  --class org.apache.spark.examples.SparkPi ../examples/jars/spark-examples_2.11-2.4.6.jar 100`
  - 步骤1：在客户端提交application，首先客户端向Master申请启动Driver
  - 步骤2：Master随机在一台Worker中启动Driver
  - 步骤3：Driver启动之后，向Master申请资源，Master返回资源
  - 步骤4：Driver发送task，监控task，回收结果
3. Driver的功能
  - 发送task
  - 监控task
  - 申请资源
  - 回收结果
   
##### 基于YARN提交任务
1. 停掉spark集群，就是关闭standalone模式
2. 在spark-env.sh 中配置HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
3. 提交任务
4. client模式
  - 提交命令：`./spark-submit --master yarn --class org.apache.spark.examples.SparkPi ../examples/jars/spark-examples_2.11-2.4.6.jar 1`
  - 步骤1：在客户端提交application，Driver会在客户端启动
  - 步骤2：客户端向RM(ResourceManager)申请启动AM（ApplicationMaster）
  - 步骤3：RM收到请求之后，随机在一台NM节点上启动AM
  - 步骤4：AM启动之后，向RM申请资源，用于启动Executor
  - 步骤5：RM收到请求之后，返回给AM一批NM节点
  - 步骤6：AM连接NM启动Executor
  - 步骤7：Executor启动之后反向注册给Driver
  - 步骤8：Driver发送task，监控task，回收结果

5. cluster模式
  - 提交命令：`./spark-submit --master yarn --deploy-model cluster --class org.apache.spark.examples.SparkPi ../examples/jars/spark-examples_2.11-2.4.6.jar 1`
  - 步骤1：在客户端提交Application，首先客户端向RM申请启动AM
  - 步骤2：RM收到请求之后，随机在一台NM节点上启动AM（Driver）
  - 步骤3：AM启动之后，向RM申请资源，用于启动Executor
  - 步骤4：RM收到请求之后，返回给AM一批NM节点
  - 步骤5：AM连接NM启动Executor
  - 步骤6：Executor启动之后反向注册给AM（Driver）
  - 步骤7：ApplicationMaster（Driver）发送task，监控task，回收结果
6. ApplicationMaster的功能
  - 申请资源
  - 启动Executor
  - 任务调度（发送任务，监控，回收结果等等）

##### mesos

##### 端口
1. 50070：HDFS WEBUI
2. 9000：HDFS通信端口
3. 8020：新版HDFS端口
4. 8088：Yarn WebUI端口
5. 2181：ZK
6. 6379：Redis
7. 60010：
8. 9083： Hive
9. 9092：Kafka

##### Spark WebUI History配置
1. 配置文件：spark-defaults.conf
2. 配置内容
```
   spark.eventLog.enabled           true
   spark.eventLog.dir               hdfs://vm01:9000/spark/data/log
   spark.history.fs.logDirectory    hdfs://vm01:9000/spark/data/log
   ```
3. 启动脚本：`sbin/stop-history-server.sh`
4. 端口 18080

## Spark SQL
### 概念
SparkSQL是一个支持使用SQL查询分布式数据的组件

##### Spark SQL 和Shark的区别
1. Spark SQL是Shark的演变
2. Spark SQL不依赖Hive
3. 可以查询原生的RDD
4. 数据封装到 DataFrame，DataFrame可以转换为RDD

##### Spark on Hive(SparkSQL)
1. Hive作为存储
2. SparkSQL作为计算
3. 解析优化也是Spark在做

##### Spark 配置Hive
1. 配置MetaStore
2. 把hive-sit.xml 拷贝到spark的conf目录
3. 打包SparkOnHiveTest程序到集群上运行

##### Hive on Spark
1. Hive承担计算和存储
2. Spark作为执行引擎

### DataFrame
DataFrame 可以转换为RDD

##### 创建DataFrame的方式
1. 读取json格式的文件，DataFrame会按照 ascii码排序
  - 读取标准的json格式文件
  - 读取嵌套的json格式文件
2. 读取json格式的RDD或者DataSet
3. 读取RDD创建DataFrame
  - 反射的方式：首先将RDD转换成某种类型的RDD(case class)，再次将rdd.toDF()
  - 动态创建Schema：动态创建的Row中的数据顺序要与创建Schema的列一致
4. 读取parquet格式数据加载DataFrame：spark.read.parquet(path)
5. 读取MySQL中的数据加载成DataFrame
6. 读取Hive中的数据加载DataFrame

##### 保存DataFrame的方式
1. 将DataFrame保存为parquet文件
2. 将DataFrame保存到MySQL表中：dataFrame.write.mode(SaveMode.Append).jdbc(url, table, connectionProperties)
3. 将DataFrame保存到Hive表

### RDD和Dataset的区别
1. todo

### UDF

## Spark Streaming
### SparkStreaming的概念
1. SparkStreaming是7*24小时不间断运行，SparkStreaming启动之后，首先会启动一个job，这个job有一个task来接收数据，task每隔一段时间将接收来的数据封装到一个batch，"这段时间"就是batchInterval。
  生产的batch又被封装到一个RDD中，这个RDD又被封装到一个DStream中，SparkStreaming底层操作的就是DStream，DStream有自己的transformation类算子（懒执行），需要DStream的outputOperator类算子触发执行
2. 因为这个间隔时间，会造成集群资源浪费的情况和数据堆积的情况。如果数据落地磁盘，又会加大数据处理的延迟度
3. 最好状态就是：batchInterval = 5s，那么集群处理一批次数据时间也是5s

### SparkStream和Storm的区别
1. Storm是纯实时处理数据，SparkStreaming是微批处理数据（有时间间隔的概念），但是可以通过时间间隔来做到实时处理，SparkStreaming相对Storm来说，吞吐量大
2. Storm擅长处理简单的汇总型业务，SparkStreaming擅长处理复杂的业务，Storm相对于SparkStreaming来说更加轻量级，SparkStreaming中可以使用core或者sql或者机器学习等等
3. Storm的事务与SparkStreaming不同，SparkStreaming可以管理事务
4. Storm支持动态的资源调度，Spark也是支持

### Driver HA
1. 在提交任务的时候指定参数 --supervise，如果Driver挂掉，会自动启动一个Driver
2. 代码层面恢复