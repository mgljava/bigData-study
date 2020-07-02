## Spark
### Spark 与 MapReduce的区别
1. Spark的迭代基于内存，MR基于磁盘
2. Spark有DAG模块

### RDD
RDD(Resilient Distributed Dataset), 弹性分布式数据集
##### RDD的五大特性
1. RDD是由一系列partition组成
2. 算子(函数)是作用在partition上的
3. RDD之间有依赖关系
4. 分区器是作用在K.V格式的RDD上
5. partition对外提供最佳的计算位置，利于数据处理的本地化

##### 算子
1. 理解为函数，例如 map、flatmap、count等等都是算子