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
2. 在驱动器程序里分发驱动器程 序中的对象集合(比如 list 和 set)

##### RDD操作
1. 转化操作(transformation)：转化操作会由一个RDD生成一个新的RDD，惰性求值
  - map操作
  - filter操作
  - union(other) 并集，重复元素不会删除
  - intersection(other) 交集，获取两个RDD中都有的元素，需要数据混洗
  - subtract(other)：移除数据，返回 一个由只存在于第一个 RDD 中而不存在于第二个 RDD 中的所有元素组成的 RDD，需要数据混洗
  - cartesian(other) ：计算两个 RDD 的笛卡儿积
2. 行动操作(action)：行动操作会对RDD计算出一个结果，并把结果返回到驱动器程序或者存储到外部存储系统(如HDFS)
  - count操作
  - first操作
  - take(num:Int)
3. 惰性求值: RDD 的转化操作都是惰性求值的。这意味着在被调用行动操作之前 Spark 不会 开始计算。

##### Spark计算数据流程
1. 从外部数据创建出输入RDD
2. 使用诸如filter()这样的转化操作对RDD进行转化，以定义新的RDD
3. 告诉spark对需要被重用的中间结果RDD执行persist()操作
4. 使用行动操作（例如count()和first()等）来触发一次并行计算，Spark会对计算进行优化后再执行