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
理解为函数，例如 map、flatmap、count等等都是算子

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