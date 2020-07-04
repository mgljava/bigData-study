## Spark
Spark是分布式计算框架，底层操作的就是RDD

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
