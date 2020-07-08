### Spark
##### Spark 组件
1. Spark Core：Spark Core 实现了 Spark 的基本功能，包含任务调度、内存管理、错误恢复、与存储系统交互等模块.还包含了对弹性分布式数据集(resilient distributed dataset，简 称 RDD)的 API 定义。
2. Spark SQL：Spark 用来操作结构化数据的程序包,通过 Spark SQL，我们可以使用 SQL 或者 Apache Hive 版本的 SQL 方言(HQL)来查询数据
3. Spark Streaming：Spark Streaming 是 Spark 提供的对实时数据进行流式计算的组件。
4. MLlib：提供常见的机器学习(ML)功能的程序库。
5. GraphX：GraphX 是用来操作图(比如社交网络的朋友关系图)的程序库
![Spark组件](https://github.com/mgljava/bigData-study/blob/master/doc/images/spark组件.png)

## 第三章 RDD编程
### RDD基础
Spark 中的 RDD 就是一个不可变的分布式对象集合。每个 RDD 都被分为多个分区，这些 分区运行在集群中的不同节点上。RDD 可以包含 Python、Java、Scala 中任意类型的对象， 甚至可以包含用户自定义的对象。

##### 创建RDD
1. 读取一个外部数据集: sc.textFile("README.md")
2. sc.parallelize(List(1,2,3))
3. sc.makeRDD(List(1,2,3))

##### RDD操作
1. 转化操作(transformation)：转化操作会由一个RDD生成一个新的RDD，惰性求值
  - map操作：将函数应用于 RDD 中的每个元 素，将返回值构成新的 RDD
  - flatMap()：将函数应用于 RDD 中的每个元 素，将返回的迭代器的所有内 容构成新的 RDD。通常用来切 分单词
  - filter操作：返回一个由通过传给 filter() 的函数的元素组成的 RDD
  - distinct()： 去重
  - union(other) 并集，重复元素不会删除
  - intersection(other) 交集，获取两个RDD中都有的元素，需要数据混洗
  - subtract(other)：移除数据，返回 一个由只存在于第一个 RDD 中而不存在于第二个 RDD 中的所有元素组成的 RDD，需要数据混洗
  - cartesian(other) ：计算两个 RDD 的笛卡儿积
  
2. 行动操作(action)：行动操作会对RDD计算出一个结果，并把结果返回到驱动器程序或者存储到外部存储系统(如HDFS)
  - count操作：会将结果回收到Driver端（程序端）
  - first操作
  - take(num:Int)：返回 RDD 中的 n 个元素
  - reduce()：它接收一个函数作为参数，这个 函数要操作两个 RDD 的元素类型的数据并返回一个同样类型的新元素
  - aggregate
  - collect(): 要求所有数据都必须能一同放入单台机器的内存中,在数据量大的情况下不适用
  - top() ：从 RDD 中获取前几个元素
  - sample(withRe placement, fra ction, [seed])：对 RDD 采样，以及是否替换
  - takeSample(withReplacement, num, seed):函数可以让我们从数据中获取一个采样，并指定是否替换。
  - foreach(): 对 RDD 中的每个元 素进行操作，而不需要把 RDD 发回本地。
  - countByValue():各元素在 RDD 中出现的次数
  - takeOrdered(num) (ordering):从 RDD 中按照提供的顺序返 回最前面的 num 个元素
3. 惰性求值: RDD 的转化操作都是惰性求值的。这意味着在被调用行动操作之前 Spark 不会 开始计算。

##### 数据持久化(缓存)
如果要缓存的数据太多，内存中放不下，Spark 会自动利用最近最少使用(LRU)的缓存 策略把最老的分区从内存中移除
1. persist(): persist() 会把数据以序列化的形式缓存在 JVM 的堆空间中
2. unpersist(): 调用该方法可以手动把持久化的 RDD 从缓 存中移除。

##### Spark计算数据流程
1. 从外部数据创建出输入RDD
2. 使用诸如filter()这样的转化操作对RDD进行转化，以定义新的RDD
3. 告诉spark对需要被重用的中间结果RDD执行persist()操作
4. 使用行动操作（例如count()和first()等）来触发一次并行计算，Spark会对计算进行优化后再执行

### 第五章 数据读取与保存
#####  文件格式：Spark 会根据文件扩展名选择对应的处理方式
1. 文本文件：普通的文本文件，每行一条记录
  - 读取文件：sc.textFile(...), sc.wholeTextFile(...)
  - 保存文件：saveAsTextFile(dir),Spark 将传入的路径作为目录对待
2. JSON
  - 读取JSON文件：ObjectMapper对象
  - 存储json数据：saveAsTextFile(...)
3. CSV:逗号分隔值与制表符分隔值
4. SequenceFiles
  - 读取：sc.sequenceFile(path="./data/spark/files/sequencefile", classOf[Text], classOf[IntWritable])
  - 写入：saveAsSequenceFile
5. Protocol buffers
6. 对象文件
  - 读取：objectFile
  - 写入：saveAsObjectFile
7. hadoop文件
  - 读取：sc.newAPIHadoopFile、hadoopDataset、newAPIHadoopDataset
  - 写入：saveAsNewAPIHadoopFile、saveAsHadoopDataSet、saveAsNewAPIHadoopDataset

##### 文件系统
1. 本地文件系统：(file:///home/root/test.txt)
2. Amazon S3: s3n://bucket/path-within-bucket
3. Hadoop: hdfs://master:port/path

##### Spark SQL
我们把一条 SQL 查询给 Spark SQL，让它对一个数据源执行查询(选出 一些字段或者对字段使用一些函数)，然后得到由 Row 对象组成的 RDD，每个 Row 对象 表示一条记录
1. Apache Hive（SparkSQLHiveTest）：要把 Spark SQL 连接到已有的 Hive 上，你需要提供 Hive 的配置文件。你需要将 hive-site. xml 文件复制到 Spark 的 ./conf/ 目录下。这样做好之后，再创建出 HiveContext 对象，也 就是 Spark SQL 的入口，然后你就可以使用 Hive 查询语言(HQL)来对你的表进行查询， 并以由行组成的 RDD 的形式拿到返回数据
2. JSON

### 第六章 Spark编程进阶
共享变量是一种可以在 Spark 任务中使用的特 殊类型的变量，Spark有两个共享变量：累加器和广播变量
##### 累加器 AddBlank.scala
概念：提供了将工作节点中的值聚合到驱动器程序中的简单语法  
累加器的用法
1. 在程序中调用 SparkContext.accumulator(initialValue)方法创建出具有初始值的累加器
2. 使用累加器的+=方法增加累加器的值
3. 使用Action算子触发执行，然后通过value属性来访问累加器的值

##### 累加器与容错性
Spark 会自动重新执行失败的或较慢的任务来应对有错误的或者比较慢的机器。例如，如 果对某分区执行 map() 操作的节点失败了，Spark 会在另一个节点上重新运行该任务  
1. 对于要在行动操作中使用的累加器，Spark 只会把每个任务对各累加器的修改应用一次，所以为了得到可靠的累加器，我们必须要放在foreach这样的Action算子中
2. 如果要在转换操作中使用累加器，那么最好是在调试的时候使用

##### 自定义累加器
通过扩展 AccumulatorParam来实现自定义累加器，累加器中的操作要满足交换律和结合律

##### 广播变量
它可以让程序高效地向所有工作节点发送一个 较大的只读值，以供一个或多个 Spark 操作使用
1. sc.broadcast(T)
2. 广播变量的用法
  - 调用SparkContext.broadcast创建一个Broadcast[T]对象
  - 通过value属性访问该对象的值
  - 变量只会被发到各个节点一次，应该作为只读来处理

##### 基于分区进行操作
1. mapPartitions: 该分区中元素的迭代器,返回的元素的迭代器
2. mapPartitionsWithIndex:分区序号，以及每个分区中,返回的元素的迭代器 的元素的迭代器
3. foreachPartitions:元素迭代器

##### 数值RDD的操作
Spark 的数值操作是通过流式算法实现的，允许以每次一个元素的方式构建出模型  
1. StatsCounter
  - count() RDD 中的元素个数 
  - mean() 元素的平均值 
  - sum() 总和
  - max() 最大值
  - min() 最小值
  - variance() 元素的方差 sampleVariance() 从采样中计算出的方差 
  - stdev() 标准差
  - sampleStdev() 采样的标准差
